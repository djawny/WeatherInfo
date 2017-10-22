package com.daniel.jawny.weatherinfo.ui.main.current;

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

public class CurrentPresenterTest {

    private static final int CITY_ID = 123456;

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    CurrentView mCurrentView;

    private CurrentPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        mPresenter = new CurrentPresenter(mDataManager, new SchedulerProviderImpl());
        mPresenter.onAttach(mCurrentView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnAttachWhenNullView() {
        mPresenter.onAttach(null);
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenNoException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.just(new City()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mCurrentView).displayCityData(any(City.class));
        verify(mCurrentView).animateViews();
    }

    @Test
    public void testLoadCityFromDatabaseByCityIdWhenException() {
        when(mDataManager.getCityFromDatabase(anyInt())).thenReturn(Observable.error(new Throwable()));

        mPresenter.loadCityFromDatabaseByCityId(CITY_ID);

        verify(mCurrentView).showDatabaseErrorInfo();
    }
}