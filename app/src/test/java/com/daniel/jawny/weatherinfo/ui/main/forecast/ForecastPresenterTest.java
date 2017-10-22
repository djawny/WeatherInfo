package com.daniel.jawny.weatherinfo.ui.main.forecast;

import com.daniel.jawny.weatherinfo.data.DataManager;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.daniel.jawny.weatherinfo.util.SchedulerProviderImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ForecastPresenterTest {

    private static final int CITY_ID = 123456;
    private static final List<Forecast> MANY_FORECASTS = Arrays.asList(new Forecast(), new Forecast());

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    ForecastView mForecastView;

    private ForecastPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        mPresenter = new ForecastPresenter(mDataManager, new SchedulerProviderImpl());
        mPresenter.onAttach(mForecastView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnAttachWhenNullView() {
        mPresenter.onAttach(null);
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenNoException() {
        City city = new City();
        city.setForecastCollection(MANY_FORECASTS);
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.just(city));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mForecastView).drawForecastBarChart(anyList());
        verify(mForecastView).drawForecastLineChart(anyList());
        verify(mForecastView).animateCharts();
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mForecastView).showDatabaseErrorInfo();
    }
}