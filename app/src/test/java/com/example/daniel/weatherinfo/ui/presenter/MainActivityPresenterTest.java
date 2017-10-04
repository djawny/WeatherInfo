package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.ui.view.MainActivityView;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainActivityPresenterTest {

    private static final int CITY_ID = 123456;
    private static final String CITY_NAME = "London";
    private static final String API_KEY = "123bla";
    private static final String LANGUAGE = "pl";
    private static final List<City> MANY_CITIES = Arrays.asList(new City(), new City());
    private static final List<Forecast> MANY_FORECASTS = Arrays.asList(new Forecast(), new Forecast());

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    MainActivityView mMainActivityView;

    private MainActivityPresenter mPresenter;

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
        mPresenter = new MainActivityPresenter(mDataManager, new SchedulerProviderImpl());
        mPresenter.setView(mMainActivityView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetViewWhenNullView() {
        mPresenter.setView(null);
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenNoEmptyListNoCurrentCity() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(MANY_CITIES));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainActivityView).setSpinnerList(ArgumentMatchers.<City>anyList());
        verify(mMainActivityView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenNoEmptyListWithCurrentCity() {
        City city = new City();
        city.setId(CITY_ID);
        List<City> cities = Arrays.asList(city, new City());

        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(cities));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainActivityView).setSpinnerList(ArgumentMatchers.<City>anyList());
        verify(mMainActivityView).displayCityData(any(City.class));
    }


    @Test
    public void testLoadCitiesFromDatabaseWhenEmptyList() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(Collections.<City>emptyList()));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainActivityView).showNoData();
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenException() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.<List<City>>error(new Throwable()));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainActivityView).showDatabaseErrorInfo();
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenNoException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.just(new City()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mMainActivityView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.<City>error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mMainActivityView).showDatabaseErrorInfo();
    }

    @Test
    public void testLoadCityFromDatabaseByCityNameWhenNoException() {
        when(mDataManager.getCityFromDatabase(anyString())).thenReturn(Observable.just(new City()));

        mPresenter.loadCityFromDatabaseByCityName(CITY_NAME);

        verify(mMainActivityView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCityFromDatabaseByCityNameWhenException() {
        when(mDataManager.getCityFromDatabase(anyString())).thenReturn(Observable.<City>error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityName(CITY_NAME);

        verify(mMainActivityView).showDatabaseErrorInfo();
    }

    @Test
    public void testRefreshCityFromNetworkWhenNoException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.complete());

        mPresenter.refreshCityFromNetwork(API_KEY, CITY_ID, LANGUAGE);

        verify(mMainActivityView).reloadData(anyInt());
    }

    @Test
    public void testRefreshCityFromNetworkWhenException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.refreshCityFromNetwork(API_KEY, CITY_ID, LANGUAGE);

        verify(mMainActivityView).showNetworkErrorInfo();
    }

    @Test
    public void testLoadCurrentCityIdWhenNoException() {
        when(mDataManager.getCurrentCityId()).thenReturn(Observable.just(CITY_ID));

        mPresenter.loadCurrentCityId();

        verify(mMainActivityView).setCurrentCityId(anyInt());
    }
}