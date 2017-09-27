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

    private final List<City> MANY_CITIES = Arrays.asList(new City(), new City(), new City());
    private final List<Forecast> MANY_FORECASTS = Arrays.asList(new Forecast(), new Forecast(), new Forecast());

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
    public void setViewShouldThrowExceptionWhenNullView() {
        mPresenter.setView(null);
    }

//    @Test
//    public void loadCitiesFromDatabaseShouldDisplayCityDataWhenNoEmptyList() {
//        when(mDataManager.getCities()).thenReturn(Observable.just(MANY_CITIES));
//
//        mPresenter.loadCitiesFromDatabase(anyInt());
//
//        verify(mMainActivityView).displayCityData(any(City.class));
//    }
//
//    @Test
//    public void loadCitiesFromDatabaseShouldShowNoDataWhenEmptyList() {
//        when(mDataManager.getCities()).thenReturn(Observable.just(Collections.<City>emptyList()));
//
//        mPresenter.loadCitiesFromDatabase(anyInt());
//
//        verify(mMainActivityView).showNoData();
//    }
//
//    @Test
//    public void loadCitiesFromDatabaseShouldShowDatabaseErrorInfoWhenException() {
//        when(mDataManager.getCities()).thenReturn(Observable.<List<City>>error(new Throwable()));
//
//        mPresenter.loadCitiesFromDatabase(anyInt());
//
//        verify(mMainActivityView).showDatabaseErrorInfo();
//    }
//
//    @Test
//    public void loadCityFromDatabaseShouldDisplayCityDataWhenNoException() {
//        when(mDataManager.getCity(anyInt())).thenReturn(Observable.just(new City()));
//
//        mPresenter.loadCityFromDatabase(anyInt());
//
//        verify(mMainActivityView).displayCityData(any(City.class));
//    }

    @Test
    public void loadCityFromDatabaseShouldShowDatabaseErrorInfoWhenException() {
        when(mDataManager.getCity(anyInt())).thenReturn(Observable.<City>error(new Throwable()));

        mPresenter.loadCityFromDatabase(anyInt());

        verify(mMainActivityView).showDatabaseErrorInfo();
    }

    @Test
    public void refreshCityFromNetworkShouldReloadData() {
        when(mDataManager.getCityWeatherDataById(anyString(),anyInt())).thenReturn(Observable.just(new CityWeatherData()));
        when(mMapper.mapCity(any(CityWeatherData.class))).thenReturn(new City());
        when(mDataManager.getCityForecastDataById(anyString(),anyInt())).thenReturn(Observable.just(new CityForecastData()));
        when(mMapper.mapForecast(any(CityForecastData.class),any(City.class))).thenReturn(MANY_FORECASTS);
        when(mDataManager.saveCity(any(City.class))).thenReturn(Completable.complete());

        mPresenter.refreshCityFromNetwork(anyString(), anyInt());

        verify(mMainActivityView).reloadData(anyInt());
    }

    @Test
    public void refreshCityFromNetworkShouldShowNetworkErrorInfo() {
        when(mDataManager.getCityWeatherDataById(anyString(),anyInt())).thenReturn(Observable.just(new CityWeatherData()));
        when(mMapper.mapCity(any(CityWeatherData.class))).thenReturn(new City());
        when(mDataManager.getCityForecastDataById(anyString(),anyInt())).thenReturn(Observable.just(new CityForecastData()));
        when(mMapper.mapForecast(any(CityForecastData.class),any(City.class))).thenReturn(MANY_FORECASTS);
        when(mDataManager.saveCity(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.refreshCityFromNetwork(anyString(), anyInt());

        verify(mMainActivityView).showNetworkErrorInfo();
    }
}