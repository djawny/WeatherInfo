package com.daniel.jawny.weatherinfo.ui.main;

import com.daniel.jawny.weatherinfo.data.DataManager;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.daniel.jawny.weatherinfo.util.SchedulerProviderImpl;

import org.junit.Assert;
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

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

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
    MainView mMainView;

    private MainPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        mPresenter = new MainPresenter(mDataManager, new SchedulerProviderImpl());
        mPresenter.onAttach(mMainView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnAttachWhenNullView() {
        mPresenter.onAttach(null);
    }

    @Test
    public void testOnDetach() {
        mPresenter.onDetach();

        assertEquals(mPresenter.getView(),null);
    }

    @Test
    public void testSetUpView() {
        mPresenter.setUpView();

        verify(mMainView).showSplashDialog();
        verify(mMainView).setToolbar();
        verify(mMainView).setSwipeRefreshListener();
        verify(mMainView).setViewPagerListener();
        verify(mMainView).setGyroscopeForPanoramaImageView();
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenNoEmptyListNoCurrentCity() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(MANY_CITIES));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainView).setSpinnerList(ArgumentMatchers.anyList());
        verify(mMainView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenNoEmptyListWithCurrentCity() {
        City city = new City();
        city.setId(CITY_ID);
        List<City> cities = Arrays.asList(city, new City());

        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(cities));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainView).setSpinnerList(ArgumentMatchers.anyList());
        verify(mMainView).displayCityData(any(City.class));
    }


    @Test
    public void testLoadCitiesFromDatabaseWhenEmptyList() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(Collections.emptyList()));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainView).showNoData();
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenException() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.error(new Throwable()));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainView).showDatabaseErrorInfo();
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenNoException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.just(new City()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mMainView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mMainView).showDatabaseErrorInfo();
    }

    @Test
    public void testLoadCityFromDatabaseByCityNameWhenNoException() {
        when(mDataManager.getCityFromDatabase(anyString())).thenReturn(Observable.just(new City()));

        mPresenter.loadCityFromDatabaseByCityName(CITY_NAME);

        verify(mMainView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCityFromDatabaseByCityNameWhenException() {
        when(mDataManager.getCityFromDatabase(anyString())).thenReturn(Observable.error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityName(CITY_NAME);

        verify(mMainView).showDatabaseErrorInfo();
    }

    @Test
    public void testRefreshCityFromNetworkWhenNoException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.complete());

        mPresenter.refreshCityFromNetwork(API_KEY, CITY_ID, LANGUAGE, true);

        verify(mMainView).reloadData(anyInt());
        verify(mMainView).hideSwipeRefreshLayoutProgress();
    }

    @Test
    public void testRefreshCityFromNetworkWhenException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.refreshCityFromNetwork(API_KEY, CITY_ID, LANGUAGE, true);

        verify(mMainView).showNetworkErrorInfo();
        verify(mMainView).hideSwipeRefreshLayoutProgress();
    }

    @Test
    public void testRefreshCityFromNetworkWhenOffline() {
        mPresenter.refreshCityFromNetwork(API_KEY, CITY_ID, LANGUAGE, false);

        verify(mMainView).showNetworkOfflineInfo();
        verify(mMainView).hideSwipeRefreshLayoutProgress();
    }

    @Test
    public void testLoadCurrentCityId() {
        when(mDataManager.getCurrentCityId()).thenReturn(Observable.just(CITY_ID));

        mPresenter.loadCurrentCityId();

        verify(mMainView).setCurrentCityId(anyInt());
    }

    @Test
    public void testSaveCurrentCityId() {
        when(mDataManager.saveCurrentCityId(anyInt())).thenReturn(Completable.complete());

        mPresenter.saveCurrentCityId(CITY_ID);

        verify(mMainView).logCurrentCityIdSaved();
    }
}