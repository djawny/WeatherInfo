package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;
import com.example.daniel.weatherinfo.ui.view.MainActivityView;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainActivityPresenterTest {

    public static final int CITY_ID = 123456;
    public static final String CITY_NAME = "London";
    public static final String API_KEY = "123bla";
    private final List<City> MANY_CITIES = Arrays.asList(new City(), new City());
    private final List<Forecast> MANY_FORECASTS = Arrays.asList(new Forecast(), new Forecast());

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    SchedulerProvider mSchedulerProvider;

    @Mock
    MainActivityView mMainActivityView;

    @Mock
    Mapper mMapper;

    @InjectMocks
    private MainActivityPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        mPresenter.setSubscribeScheduler(Schedulers.trampoline());
        mPresenter.setObserveScheduler(Schedulers.trampoline());
        mPresenter.setView(mMainActivityView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetViewWhenNullView() {
        mPresenter.setView(null);
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenNoEmptyListNoCurrentCity() {
        when(mDataManager.getCities()).thenReturn(Observable.just(MANY_CITIES));

        mPresenter.loadCitiesFromDatabase(CITY_ID);


        verify(mMainActivityView).setSpinnerList(ArgumentMatchers.<City>anyList());
        verify(mMainActivityView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenNoEmptyListWithCurrentCity() {
        City city = new City();
        city.setId(CITY_ID);
        List<City> cities = Arrays.asList(city, new City());

        when(mDataManager.getCities()).thenReturn(Observable.just(cities));

        mPresenter.loadCitiesFromDatabase(CITY_ID);


        verify(mMainActivityView).setSpinnerList(ArgumentMatchers.<City>anyList());
        verify(mMainActivityView).displayCityData(any(City.class));
    }


    @Test
    public void testLoadCitiesFromDatabaseWhenEmptyList() {
        when(mDataManager.getCities()).thenReturn(Observable.just(Collections.<City>emptyList()));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainActivityView).showNoData();
    }

    @Test
    public void testLoadCitiesFromDatabaseWhenException() {
        when(mDataManager.getCities()).thenReturn(Observable.<List<City>>error(new Throwable()));

        mPresenter.loadCitiesFromDatabase(CITY_ID);

        verify(mMainActivityView).showDatabaseErrorInfo();
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenNoException() {
        when(mDataManager.getCity(anyInt())).thenReturn(Observable.just(new City()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mMainActivityView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenException() {
        when(mDataManager.getCity(anyInt())).thenReturn(Observable.<City>error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mMainActivityView).showDatabaseErrorInfo();
    }

    @Test
    public void testLoadCityFromDatabaseByCityNameWhenNoException() {
        when(mDataManager.getCity(anyString())).thenReturn(Observable.just(new City()));

        mPresenter.loadCityFromDatabaseByCityName(CITY_NAME);

        verify(mMainActivityView).displayCityData(any(City.class));
    }

    @Test
    public void testLoadCityFromDatabaseByCityNameWhenException() {
        when(mDataManager.getCity(anyString())).thenReturn(Observable.<City>error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityName(CITY_NAME);

        verify(mMainActivityView).showDatabaseErrorInfo();
    }

    @Test
    public void testRefreshCityFromNetworkWhenNoException() {
        when(mDataManager.getCityWeatherDataById(anyString(), anyInt())).thenReturn(Observable.just(new CityWeatherData()));
        when(mMapper.mapCity(any(CityWeatherData.class))).thenReturn(new City());
        when(mDataManager.getCityForecastDataById(anyString(), anyInt())).thenReturn(Observable.just(new CityForecastData()));
        when(mMapper.mapForecast(any(CityForecastData.class), any(City.class))).thenReturn(MANY_FORECASTS);
        when(mDataManager.saveCity(any(City.class))).thenReturn(Completable.complete());

        mPresenter.refreshCityFromNetwork(API_KEY, CITY_ID);

        verify(mMainActivityView).reloadData(anyInt());
    }

    @Test
    public void testRefreshCityFromNetworkWhenException() {
        when(mDataManager.getCityWeatherDataById(anyString(), anyInt())).thenReturn(Observable.just(new CityWeatherData()));
        when(mMapper.mapCity(any(CityWeatherData.class))).thenReturn(new City());
        when(mDataManager.getCityForecastDataById(anyString(), anyInt())).thenReturn(Observable.just(new CityForecastData()));
        when(mMapper.mapForecast(any(CityForecastData.class), any(City.class))).thenReturn(MANY_FORECASTS);
        when(mDataManager.saveCity(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.refreshCityFromNetwork(API_KEY, CITY_ID);

        verify(mMainActivityView).showNetworkErrorInfo();
    }

    @Test
    public void testInitializeCurrentCityIdWhenNoException() {
        when(mDataManager.getIntSharedPreferences(anyString())).thenReturn(Observable.just(CITY_ID));

        mPresenter.initializeCurrentCityId();

        verify(mMainActivityView).setCurrentCityId(anyInt());
    }
}