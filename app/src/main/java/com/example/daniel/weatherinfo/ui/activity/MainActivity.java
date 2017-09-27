package com.example.daniel.weatherinfo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.adapter.MainPagerAdapter;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;
import com.example.daniel.weatherinfo.ui.fragment.CurrentFragment;
import com.example.daniel.weatherinfo.ui.fragment.ForecastFragment;
import com.example.daniel.weatherinfo.ui.presenter.MainActivityPresenter;
import com.example.daniel.weatherinfo.ui.view.MainActivityView;
import com.example.daniel.weatherinfo.util.BackgroundProvider;
import com.example.daniel.weatherinfo.util.NetworkUtils;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.daniel.weatherinfo.ui.activity.CityListActivity.CITY_ID;
import static com.example.daniel.weatherinfo.ui.activity.CityListActivity.CITY_LIST_HAS_BEEN_CHANGED_FLAG;

public class MainActivity extends BaseActivity implements MainActivityView, SwipeRefreshLayout.OnRefreshListener,
        ViewPager.OnPageChangeListener, AdapterView.OnItemSelectedListener {

    private static final int CITY_LIST_REQUEST_CODE = 1;

    private int mCurrentCityId;

    @BindView(R.id.bg_image_view)
    PanoramaImageView mBackground;

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

    @BindView(R.id.spinner)
    Spinner mSpinner;

    @Inject
    MainActivityPresenter mPresenter;

    private MainPagerAdapter mPagerAdapter;
    private ArrayAdapter<String> mSpinnerAdapter;
    private GyroscopeObserver mGyroscopeObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        mPresenter.setView(this);
        setToolbar();
        setSwipeRefreshListener();
        setViewPagerListener();
        setGyroscopeForPanoramaImageView();
        mPresenter.initializeCurrentCityId();
        mPresenter.loadCitiesFromDatabase(mCurrentCityId);
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setSwipeRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
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

    private void setGyroscopeForPanoramaImageView() {
        mGyroscopeObserver = new GyroscopeObserver();
        mGyroscopeObserver.setMaxRotateRadian(Math.PI / 2);
        mBackground.setGyroscopeObserver(mGyroscopeObserver);
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
    protected void onResume() {
        super.onResume();
        mGyroscopeObserver.register(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CITY_LIST_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                int cityId = bundle.getInt(CITY_ID);
                if (cityId != 0) {
                    mCurrentCityId = cityId;
                }
                if (bundle.getBoolean(CITY_LIST_HAS_BEEN_CHANGED_FLAG)) {
                    mPresenter.loadCitiesFromDatabase(mCurrentCityId);
                } else {
                    mPresenter.loadCityFromDatabase(mCurrentCityId);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGyroscopeObserver.unregister();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.saveCurrentCity(mCurrentCityId);
        mPresenter.clearDisposable();
    }

    @Override
    public void setCurrentCityId(Integer cityId) {
        mCurrentCityId = cityId;
    }

    @Override
    public void setSpinnerList(List<City> cities) {
        List<String> citiesNameList = new ArrayList<>();
        for (City city : cities) {
            citiesNameList.add(city.getName());
        }
        if (mSpinnerAdapter == null) {
            initializeSpinner(citiesNameList);
        } else {
            updateSpinner(citiesNameList);
        }
    }

    private void initializeSpinner(List<String> citiesNameList) {
        mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_header, citiesNameList);
        mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void updateSpinner(List<String> citiesNameList) {
        mSpinnerAdapter.clear();
        mSpinnerAdapter.addAll(citiesNameList);
        mSpinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayCityData(City city) {
        if (mStatusInfo.getVisibility() == View.VISIBLE) {
            mStatusInfo.setVisibility(View.GONE);
            mSpinner.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        if (mPagerAdapter == null) {
            initializeViewPager(city);
        } else {
            mPagerAdapter.swapData(city);
        }
        mCurrentCityId = city.getId();
        mSpinner.setSelection(getSpinnerItemIndex(city.getName()));
        mBackground.setImageResource(BackgroundProvider.getBackground(city.getWeather().getIcon()));
        hideSwipeRefreshLayoutProgressSpinner();
    }

    private void initializeViewPager(City city) {
        String[] tabTitles = getResources().getStringArray(R.array.tab_titles);
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), tabTitles, city);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void showNoData() {
        mSpinner.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mStatusInfo.setVisibility(View.VISIBLE);
        mBackground.setImageResource(R.drawable.default_bg);
    }

    private int getSpinnerItemIndex(String myString) {
        int index = 0;
        for (int i = 0; i < mSpinner.getCount(); i++) {
            if (mSpinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void showDatabaseErrorInfo() {
        showSnackBar(getString(R.string.message_error_loading_deleting_data), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showNetworkErrorInfo() {
        hideSwipeRefreshLayoutProgressSpinner();
        showSnackBar(getString(R.string.message_error_loading_data_from_network), Snackbar.LENGTH_LONG);
    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            mPresenter.refreshCityFromNetwork(getString(R.string.open_weather_map_api_key), mCurrentCityId);
        } else {
            hideSwipeRefreshLayoutProgressSpinner();
            showSnackBar(getString(R.string.message_network_connection_error), Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void reloadData(int cityId) {
        mPresenter.loadCityFromDatabase(cityId);
        hideSwipeRefreshLayoutProgressSpinner();
    }

    public void hideSwipeRefreshLayoutProgressSpinner() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //ignore
    }

    @Override
    public void onPageSelected(int position) {
        if (mPagerAdapter != null) {
            Fragment fragment = (Fragment) mPagerAdapter.instantiateItem(mViewPager, position);
            if (fragment instanceof CurrentFragment) {
                ((CurrentFragment) fragment).animateViews();
                mSwipeRefreshLayout.setEnabled(true);
            } else if (fragment instanceof ForecastFragment) {
                ((ForecastFragment) fragment).animateViews();
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //ignore
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String cityName = parent.getItemAtPosition(position).toString();
        mPresenter.loadCityFromDatabase(cityName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //ignore
    }
}
