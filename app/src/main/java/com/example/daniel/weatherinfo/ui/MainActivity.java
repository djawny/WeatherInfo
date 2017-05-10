package com.example.daniel.weatherinfo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.api.OpenWeatherMapService;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.ui.adapter.CityPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, MainActivityView {

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

    private CityPagerAdapter mCityPagerAdapter;
    private boolean mPullRefreshing;
    private MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mPresenter = new MainActivityPresenter(CityRepository.getInstance(),
                OpenWeatherMapService.Factory.makeWeatherService(), Schedulers.io(), AndroidSchedulers.mainThread());
        mPresenter.setView(this);
        loadCities();
        setPageChangeListener();
        setPullRefresh();
    }

    private void loadCities() {
        mPresenter.loadCitiesFromDatabase();
        //Todo
//        if (NetworkUtils.isNetAvailable(this)) {
//            mPresenter.loadCities();
//        } else {
//            mPresenter.loadCitiesFromDatabase();
//        }
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

    private void setPageChangeListener() {
        mViewPager.addOnPageChangeListener(this);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        City city = mCityPagerAdapter.getCities().get(position);
        mToolbar.setTitle(String.format("%s, %s", city.getName(), city.getCountry()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void showCities(List<City> cities) {
        mPullRefreshLayout.setVisibility(View.VISIBLE);
        mStatusInfo.setVisibility(View.GONE);
        if (mCityPagerAdapter == null) {
            mCityPagerAdapter = new CityPagerAdapter(getSupportFragmentManager(), cities);
            mViewPager.setAdapter(mCityPagerAdapter);
        } else {
            mCityPagerAdapter.swapData(cities);
        }

        if (mPullRefreshing) {
            mPullRefreshLayout.setRefreshing(false);
            mPullRefreshing = false;
        }

        int currentPage = mViewPager.getCurrentItem();
        mToolbar.setTitle(String.format("%s, %s", cities.get(currentPage).getName(), cities.get(currentPage).getCountry()));
    }

    @Override
    public void showNoData() {
        mPullRefreshLayout.setVisibility(View.GONE);
        mStatusInfo.setVisibility(View.VISIBLE);
        mStatusInfo.setText(R.string.message_no_data);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
    }

    @Override
    public void showErrorInfo() {
        mPullRefreshLayout.setVisibility(View.GONE);
        mStatusInfo.setVisibility(View.VISIBLE);
        mStatusInfo.setText(R.string.message_error);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
    }

    private void setPullRefresh() {
        mPullRefreshing = false;
        mPullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullRefreshing = true;
                loadCities();
            }
        });
    }
}
