package com.example.daniel.weatherinfo.ui.locations;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
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

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocationsPresenterTest {

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
    LocationsView mLocationsView;

    private LocationsPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        mPresenter = new LocationsPresenter(mDataManager, new SchedulerProviderImpl());
        mPresenter.onAttach(mLocationsView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnAttachWhenNullView() {
        mPresenter.onAttach(null);
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenNoEmptyList() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(MANY_CITIES));

        mPresenter.loadCitiesFromDatabase();

        verify(mLocationsView).displayCities(ArgumentMatchers.anyList());
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenEmptyList() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.just(Collections.emptyList()));

        mPresenter.loadCitiesFromDatabase();

        verify(mLocationsView).showNoData();
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenException() {
        when(mDataManager.getCitiesFromDatabase()).thenReturn(Observable.error(new Throwable()));

        mPresenter.loadCitiesFromDatabase();

        verify(mLocationsView).showLoadErrorInfo();
    }

    @Test
    public void testDeleteCityFromDatabaseWhenNoException() {
        when(mDataManager.removeCityFromDatabase(anyInt())).thenReturn(Completable.complete());

        mPresenter.deleteCityFromDatabase(CITY_ID);

        verify(mLocationsView).reloadData();
    }

    @Test
    public void testDeleteCityFromDatabaseWhenException() {
        when(mDataManager.removeCityFromDatabase(anyInt())).thenReturn(Completable.error(new Throwable()));

        mPresenter.deleteCityFromDatabase(CITY_ID);

        verify(mLocationsView).showDeleteErrorInfo();
    }

    @Test
    public void testAddCityFromNetworkWhenNoException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.complete());

        mPresenter.addCityFromNetwork(API_KEY, LATITUDE, LONGITUDE, LANGUAGE);

        verify(mLocationsView).hideAddLocationProgressBar();
        verify(mLocationsView).reloadData();
    }

    @Test
    public void testAddCityFromNetworkWhenException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.addCityFromNetwork(API_KEY, LATITUDE, LONGITUDE, LANGUAGE);

        verify(mLocationsView).hideAddLocationProgressBar();
        verify(mLocationsView).showNetworkErrorInfo();
    }

    @Test
    public void testLoadCityFromNetworkWhenNoException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.just(new City()));
        when(mDataManager.getForecastsFromNetwork(anyString(), anyInt(), anyString())).thenReturn(Observable.just(MANY_FORECASTS));

        mPresenter.loadCityFromNetwork(API_KEY, LATITUDE, LONGITUDE, LANGUAGE);

        verify(mLocationsView).hideActualLocationProgressBar();
        verify(mLocationsView).updateActualLocationText(any(City.class));
    }

    @Test
    public void testLoadCityFromNetworkWhenException() {
        when(mDataManager.getCityFromNetwork(anyString(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.error(new Throwable()));

        mPresenter.loadCityFromNetwork(API_KEY, LATITUDE, LONGITUDE, LANGUAGE);

        verify(mLocationsView).hideActualLocationProgressBar();
        verify(mLocationsView).showNetworkErrorInfo();
    }

    @Test
    public void testSaveCityToDatabaseCityWhenNoException() {
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.complete());

        mPresenter.saveCityToDatabase(new City());

        verify(mLocationsView).hideAddLocationProgressBar();
        verify(mLocationsView).reloadData();
    }

    @Test
    public void testSaveCityToDatabaseCityWhenException() {
        when(mDataManager.saveCityToDatabase(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.saveCityToDatabase(new City());

        verify(mLocationsView).hideAddLocationProgressBar();
        verify(mLocationsView).showSaveErrorInfo();
    }
}