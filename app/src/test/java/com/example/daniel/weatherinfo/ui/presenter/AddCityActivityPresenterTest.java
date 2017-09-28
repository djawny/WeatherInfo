package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;
import com.example.daniel.weatherinfo.ui.view.AddCityActivityView;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddCityActivityPresenterTest {

    public static final String API_KEY = "123bla";
    public static final String CITY_NAME = "London";
    private final List<Forecast> MANY_FORECASTS = Arrays.asList(new Forecast(), new Forecast(), new Forecast());

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    SchedulerProvider mSchedulerProvider;

    @Mock
    AddCityActivityView mAddCityActivityView;

    @Mock
    Mapper mMapper;

    @InjectMocks
    private AddCityActivityPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        mPresenter.setSubscribeScheduler(Schedulers.trampoline());
        mPresenter.setObserveScheduler(Schedulers.trampoline());
        mPresenter.setView(mAddCityActivityView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetViewWhenNullView() {
        mPresenter.setView(null);
    }

    @Test
    public void testRefreshCityFromNetworkWhenNoException() {
        when(mDataManager.getCityWeatherDataByName(anyString(), anyString())).thenReturn(Observable.just(new CityWeatherData()));
        when(mMapper.mapCity(any(CityWeatherData.class))).thenReturn(new City());
        when(mDataManager.getCityForecastDataById(anyString(), anyInt())).thenReturn(Observable.just(new CityForecastData()));
        when(mMapper.mapForecast(any(CityForecastData.class), any(City.class))).thenReturn(MANY_FORECASTS);
        when(mDataManager.saveCity(any(City.class))).thenReturn(Completable.complete());

        mPresenter.addCityFromNetwork(API_KEY, CITY_NAME);

        verify(mAddCityActivityView).closeScreen();
    }

    @Test
    public void testRefreshCityFromNetworkWhenException() {
        when(mDataManager.getCityWeatherDataByName(anyString(), anyString())).thenReturn(Observable.just(new CityWeatherData()));
        when(mMapper.mapCity(any(CityWeatherData.class))).thenReturn(new City());
        when(mDataManager.getCityForecastDataById(anyString(), anyInt())).thenReturn(Observable.just(new CityForecastData()));
        when(mMapper.mapForecast(any(CityForecastData.class), any(City.class))).thenReturn(MANY_FORECASTS);
        when(mDataManager.saveCity(any(City.class))).thenReturn(Completable.error(new Throwable()));

        mPresenter.addCityFromNetwork(API_KEY, CITY_NAME);

        verify(mAddCityActivityView).showNetworkErrorInfo();
    }


}