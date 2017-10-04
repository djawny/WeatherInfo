package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;
import com.example.daniel.weatherinfo.ui.view.CityListActivityView;
import com.example.daniel.weatherinfo.util.SchedulerProviderImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CityListActivityPresenterTest {

    private static final int CITY_ID = 123456;
    private static final String API_KEY = "123bla";
    private static final double LATITUDE = 51.50;
    private static final double LONGITUDE = 13.01;
    private static final String LANGUAGE = "pl";
    private final List<City> MANY_CITIES = Arrays.asList(new City(), new City());
    private final List<Forecast> MANY_FORECASTS = Arrays.asList(new Forecast(), new Forecast(), new Forecast());

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    CityListActivityView mCityListActivityView;

    private CityListActivityPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return Schedulers.trampoline();
            }
        });
        mPresenter = new CityListActivityPresenter(mDataManager, new SchedulerProviderImpl());
        mPresenter.setView(mCityListActivityView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetViewWhenNullView() {
        mPresenter.setView(null);
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenNoEmptyList() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(MANY_CITIES));

        mPresenter.loadCitiesFromDatabase();

        verify(mCityListActivityView).displayCities(ArgumentMatchers.<City>anyList());
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenEmptyList() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(Collections.<City>emptyList()));

        mPresenter.loadCitiesFromDatabase();

        verify(mCityListActivityView).showNoData();
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenException() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.<List<City>>error(new Throwable()));

        mPresenter.loadCitiesFromDatabase();

        verify(mCityListActivityView).showLoadErrorInfo();
    }

    @Test
    public void testDeleteCityFromDatabaseWhenNoException() {
        when(mDataManager.removeCityFromDatabase(anyInt())).thenReturn(Completable.complete());

        mPresenter.deleteCityFromDatabase(CITY_ID);

        verify(mCityListActivityView).reloadData();
    }

    @Test
    public void testDeleteCityFromDatabaseWhenException() {
        when(mDataManager.removeCityFromDatabase(anyInt())).thenReturn(Completable.error(new Throwable()));

        mPresenter.deleteCityFromDatabase(CITY_ID);

        verify(mCityListActivityView).showDeleteErrorInfo();
    }

    @Test
    public void testAddCityFromNetworkWhenNoException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.complete());

        mPresenter.addCityFromNetwork(API_KEY, LATITUDE, LONGITUDE, LANGUAGE);

        verify(mCityListActivityView).hideAddLocationProgressBar();
        verify(mCityListActivityView).reloadData();
    }

    @Test
    public void testAddCityFromNetworkWhenException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.addCityFromNetwork(API_KEY, LATITUDE, LONGITUDE, LANGUAGE);

        verify(mCityListActivityView).hideAddLocationProgressBar();
        verify(mCityListActivityView).showNetworkErrorInfo();
    }

    @Test
    public void testLoadCityFromNetworkWhenNoException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));

        mPresenter.loadCityFromNetwork(API_KEY, LATITUDE, LONGITUDE, LANGUAGE);

        verify(mCityListActivityView).hideActualLocationProgressBar();
        verify(mCityListActivityView).updateActualLocationText(any(City.class));
    }

    @Test
    public void testLoadCityFromNetworkWhenException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.<City>error(new Throwable()));

        mPresenter.loadCityFromNetwork(API_KEY, LATITUDE, LONGITUDE, LANGUAGE);

        verify(mCityListActivityView).hideActualLocationProgressBar();
        verify(mCityListActivityView).showNetworkErrorInfo();
    }

    @Test
    public void testSaveCityToDatabaseCityWhenNoException() {
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.complete());

        mPresenter.saveCityToDatabase(new City());

        verify(mCityListActivityView).hideAddLocationProgressBar();
        verify(mCityListActivityView).reloadData();
    }

    @Test
    public void testSaveCityToDatabaseCityWhenException() {
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.saveCityToDatabase(new City());

        verify(mCityListActivityView).hideAddLocationProgressBar();
        verify(mCityListActivityView).showSaveErrorInfo();
    }
}