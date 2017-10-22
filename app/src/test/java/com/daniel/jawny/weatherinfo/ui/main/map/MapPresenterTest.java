package com.daniel.jawny.weatherinfo.ui.main.map;

import com.daniel.jawny.weatherinfo.data.DataManager;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.util.SchedulerProviderImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapPresenterTest {

    private static final int CITY_ID = 123456;

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    MapView mMapView;

    private MapPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        mPresenter = new MapPresenter(mDataManager, new SchedulerProviderImpl());
        mPresenter.onAttach(mMapView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnAttachWhenNullView() {
        mPresenter.onAttach(null);
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenNoException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.just(new City()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mMapView).displayMap(any(City.class));
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mMapView).showDatabaseErrorInfo();
    }
}