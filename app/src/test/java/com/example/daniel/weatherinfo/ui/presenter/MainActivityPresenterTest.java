package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.view.MainActivityView;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainActivityPresenterTest {

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    SchedulerProvider mSchedulerProvider;

    @Mock
    MainActivityView mMainActivityView;

    private MainActivityPresenter mPresenter;
    private final List<City> MANY_CITIES = Arrays.asList(new City(), new City(), new City());

    @Before
    public void setUp() throws Exception {
        mPresenter = new MainActivityPresenter(mDataManager, mSchedulerProvider);
        mPresenter.setSubscribeScheduler(Schedulers.trampoline());
        mPresenter.setObserveScheduler(Schedulers.trampoline());
        mPresenter.setView(mMainActivityView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPresenterShouldThrowExceptionWhenNullView() {
        mPresenter.setView(null);
    }

    @Test
    public void testPresenterLoadFirstCityFromDatabaseShouldDisplayCityDataWhenNoEmptyList() {
        when(mDataManager.getCities()).thenReturn(Observable.just(MANY_CITIES));

        mPresenter.loadFirstCityFromDatabase();

        verify(mMainActivityView).displayCityData(any(City.class));
    }

    @Test
    public void testPresenterLoadFirstCityFromDatabaseShouldShowNoDataWhenEmptyList() {
        when(mDataManager.getCities()).thenReturn(Observable.just(Collections.<City>emptyList()));

        mPresenter.loadFirstCityFromDatabase();

        verify(mMainActivityView).showNoData();
    }

    @Test
    public void testPresenterLoadFirstCityFromDatabaseShouldShowErrorWhenException() {
        when(mDataManager.getCities()).thenReturn(Observable.<List<City>>error(new Throwable()));

        mPresenter.loadFirstCityFromDatabase();

        verify(mMainActivityView).showDatabaseErrorInfo();
    }

}