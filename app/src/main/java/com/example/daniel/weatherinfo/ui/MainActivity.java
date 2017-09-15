package com.example.daniel.weatherinfo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.adapter.MainPagerAdapter;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;
import com.example.daniel.weatherinfo.util.BackgroundProvider;
import com.example.daniel.weatherinfo.util.NetworkUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.daniel.weatherinfo.ui.CityListActivity.CITY_ID;

public class MainActivity extends BaseActivity implements MainActivityView, SwipeRefreshLayout.OnRefreshListener {

    private static final int CITY_LIST_REQUEST_CODE = 1;

    private int mCurrentCityId;

    private String[] mTabTitles;

    @BindView(R.id.bg_image_view)
    ImageView mBackground;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.status_info)
    TextView mStatusInfo;

    @Inject
    MainActivityPresenter mPresenter;

    private MainPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        initializeTabTitles();
        getActivityComponent().inject(this);
        setPresenter();
        setSwipeRefreshListener();
        setViewPagerListener();
        mPresenter.loadDataFromDatabase();
    }

    private void initializeTabTitles() {
        mTabTitles = getResources().getStringArray(R.array.tab_titles);
    }

    private void setSwipeRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setPresenter() {
        mPresenter.setView(this);
    }

    @Override
    public void displayData(City city) {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mStatusInfo.setVisibility(View.GONE);
        if (mPagerAdapter == null) {
            mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mTabTitles, city);
            mViewPager.setAdapter(mPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        } else {
            mPagerAdapter.swapData(city);
        }
        mCurrentCityId = city.getId();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(String.format("%s, %s", city.getName(), city.getCountry()));
        }
        mBackground.setImageResource(BackgroundProvider.getBackground(city.getWeather().getIcon()));

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showNoData() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mStatusInfo.setVisibility(View.VISIBLE);
        mBackground.setImageResource(R.drawable.bg);
    }

    @Override
    public void showErrorInfo() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        showSnackBar(getString(R.string.message_error_loading_data), Snackbar.LENGTH_LONG);
    }

    @Override
    public void onRefreshComplete() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mPresenter.loadDataByIdFromDatabase(mCurrentCityId);
    }

    private void setViewPagerListener() {
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.isNetAvailable(MainActivity.this)) {
            mPresenter.loadDataByIdFromNetwork(mCurrentCityId);
        } else {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            showSnackBar(getString(R.string.message_network_connection_error), Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_city_list:
                Intent intent = new Intent(this, CityListActivity.class);
                startActivityForResult(intent, CITY_LIST_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CITY_LIST_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                mCurrentCityId = bundle.getInt(CITY_ID);
                mPresenter.loadDataByIdFromDatabase(mCurrentCityId);
            } else {
                mPresenter.loadDataFromDatabase();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }
}
