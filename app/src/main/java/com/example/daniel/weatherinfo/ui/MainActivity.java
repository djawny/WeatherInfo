package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.adapter.CityPagerAdapter;
import com.example.daniel.weatherinfo.api.OpenWeatherMapService;
import com.example.daniel.weatherinfo.application.AndroidApplication;
import com.example.daniel.weatherinfo.database.Database;
import com.example.daniel.weatherinfo.mapper.CityMapper;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.model.OWMResponse;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Database mDatabase;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private CityPagerAdapter mCityPagerAdapter;
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setViewPager();

        if (mDatabase == null) {
            mDatabase = AndroidApplication.getDatabase();
        }

        OpenWeatherMapService.Factory.makeWeatherService().getWeatherByCity("London")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OWMResponse>() {
                    @Override
                    public void onNext(OWMResponse owmResponse) {
                        city = CityMapper.mapCity(owmResponse);
                        mDatabase.saveCity(city);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void setViewPager() {
        mCityPagerAdapter = new CityPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mCityPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
