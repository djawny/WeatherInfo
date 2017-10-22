package com.daniel.jawny.weatherinfo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
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

import com.daniel.jawny.weatherinfo.R;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.base.BaseActivity;
import com.daniel.jawny.weatherinfo.ui.locations.LocationsActivity;
import com.daniel.jawny.weatherinfo.ui.main.current.CurrentFragment;
import com.daniel.jawny.weatherinfo.ui.main.forecast.ForecastFragment;
import com.daniel.jawny.weatherinfo.ui.main.map.MapFragment;
import com.daniel.jawny.weatherinfo.util.BackgroundUtils;
import com.daniel.jawny.weatherinfo.util.LanguageUtils;
import com.daniel.jawny.weatherinfo.util.NetworkInfoUtils;
import com.daniel.jawny.weatherinfo.util.SnackBarHandler;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.daniel.jawny.weatherinfo.ui.locations.LocationsActivity.CITY_ID;
import static com.daniel.jawny.weatherinfo.ui.locations.LocationsActivity.CITY_LIST_HAS_BEEN_CHANGED_FLAG;

public class MainActivity extends BaseActivity implements MainView, SwipeRefreshLayout.OnRefreshListener,
        ViewPager.OnPageChangeListener, AdapterView.OnItemSelectedListener {

    private static final int CITY_LIST_REQUEST_CODE = 1;
    public static final String DIALOG = "dialog";

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
    MainPresenter mPresenter;

    private MainPagerAdapter mPagerAdapter;
    private ArrayAdapter<String> mSpinnerAdapter;
    private GyroscopeObserver mGyroscopeObserver;
    private DialogFragment mSplashDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        mPresenter.setUpView();
        mPresenter.loadCurrentCityId();
        mPresenter.loadCitiesFromDatabase(mCurrentCityId);
    }

    @Override
    public void showSplashDialog() {
        mSplashDialog = MainSplashDialog.newInstance();
        mSplashDialog.show(getSupportFragmentManager(), DIALOG);
    }

    @Override
    public void setToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void setSwipeRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void setViewPagerListener() {
        mViewPager.setOnTouchListener((v, event) -> {
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
        });
    }

    @Override
    public void setGyroscopeForPanoramaImageView() {
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
            case R.id.action_show_locations:
                Intent intent = new Intent(this, LocationsActivity.class);
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
                    mPresenter.loadCityFromDatabaseByCityId(mCurrentCityId);
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
        mPresenter.onDetach();
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
            initializeViewPager(city.getId());
        } else {
            mPagerAdapter.swapData(city.getId());
        }
        mCurrentCityId = city.getId();
        mSpinner.setSelection(getSpinnerItemIndex(city.getName()));
        mBackground.setImageResource(BackgroundUtils.getImageResId(city.getWeather().getIcon()));
        mPresenter.saveCurrentCity(mCurrentCityId);
    }

    private void initializeViewPager(int cityId) {
        String[] tabTitles = getResources().getStringArray(R.array.tab_titles);
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), tabTitles, cityId);
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

    @Override
    public void dismissSplashDialog() {
        if (mSplashDialog != null) {
            mSplashDialog.dismiss();
        }
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
        SnackBarHandler.show(this, getString(R.string.message_error_loading_deleting_saving_data), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showNetworkErrorInfo() {
        SnackBarHandler.show(this, getString(R.string.message_error_loading_data_from_network), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showNetworkOfflineInfo() {
        SnackBarHandler.show(this, getString(R.string.message_network_connection_error), Snackbar.LENGTH_LONG);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshCityFromNetwork(getString(R.string.open_weather_map_api_key), mCurrentCityId,
                LanguageUtils.getLocalLang(), NetworkInfoUtils.isOnline(this));
    }

    @Override
    public void reloadData(int cityId) {
        mPresenter.loadCityFromDatabaseByCityId(cityId);
    }

    @Override
    public void hideSwipeRefreshLayoutProgress() {
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
                ((ForecastFragment) fragment).animateCharts();
                mSwipeRefreshLayout.setEnabled(true);
            } else if (fragment instanceof MapFragment) {
                ((MapFragment) fragment).animateMap();
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
        mPresenter.loadCityFromDatabaseByCityName(cityName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //ignore
    }
}
