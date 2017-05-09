package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.ui.adapter.CityPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.main_background)
    ImageView mImageView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private CityPagerAdapter mCityPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setViewPager();
        mViewPager.addOnPageChangeListener(this);
    }

    private void setViewPager() {
        CityRepository.getInstance().getCitiesRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<City>>() {
                    @Override
                    public void onNext(List<City> cities) {
                        mCityPagerAdapter = new CityPagerAdapter(getSupportFragmentManager(), cities);
                        mViewPager.setAdapter(mCityPagerAdapter);
                        mToolbar.setTitle(String.format("%s, %s", cities.get(0).getName(), cities.get(0).getCountry()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_cities:

                break;
            case R.id.action_add:

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
}
