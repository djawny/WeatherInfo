package com.example.daniel.weatherinfo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.di.component.ActivityComponent;
import com.example.daniel.weatherinfo.ui.adapter.CityPagerAdapter;
import com.example.daniel.weatherinfo.ui.adapter.ZoomOutTransformer;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;
import com.example.daniel.weatherinfo.util.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainActivityView {

    private static final int ADD_CITY_REQUEST_CODE = 1;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.main_background)
    ImageView mImageView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.refresh_layout)
    PullRefreshLayout mPullRefreshLayout;

    @BindView(R.id.status_info)
    TextView mStatusInfo;

    @Inject
    MainActivityPresenter mPresenter;

    private CityPagerAdapter mCityPagerAdapter;
    private boolean mPullRefreshing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActivityComponent activityComponent = getActivityComponent();
        activityComponent.inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initializePresenter();
        initializePullRefresh();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCities();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    private void initializePresenter() {
        mPresenter.setView(this);
    }

    private void loadCities() {
        mPresenter.loadCitiesFromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddCityActivity.class);
                startActivityForResult(intent, ADD_CITY_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayCities(List<City> cities) {
        mPullRefreshLayout.setVisibility(View.VISIBLE);
        mStatusInfo.setVisibility(View.GONE);
        if (mCityPagerAdapter == null) {
            mCityPagerAdapter = new CityPagerAdapter(getSupportFragmentManager(), cities);
            mViewPager.setAdapter(mCityPagerAdapter);
            mViewPager.setPageTransformer(true,new ZoomOutTransformer());
        } else {
            mCityPagerAdapter.swapData(cities);
        }

        if (mPullRefreshing) {
            mPullRefreshLayout.setRefreshing(false);
            mPullRefreshing = false;
        }
    }

    @Override
    public void showNoData() {
        mPullRefreshLayout.setVisibility(View.GONE);
        mStatusInfo.setVisibility(View.VISIBLE);
        mStatusInfo.setText(R.string.message_no_data);
    }

    @Override
    public void showErrorInfo() {
        mPullRefreshLayout.setVisibility(View.GONE);
        mStatusInfo.setVisibility(View.VISIBLE);
        mStatusInfo.setText(R.string.message_error);
    }

    private void initializePullRefresh() {
        mPullRefreshing = false;
        mPullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullRefreshing = true;
                if (NetworkUtils.isNetAvailable(MainActivity.this)) {
                    mPresenter.loadCitiesFromNetwork();
                } else {
                    mPullRefreshing = false;
                    mPullRefreshLayout.setRefreshing(false);
                    showSnackBar("Network error! Check the network connection settings.", Snackbar.LENGTH_LONG);
                }
            }
        });
    }
}
