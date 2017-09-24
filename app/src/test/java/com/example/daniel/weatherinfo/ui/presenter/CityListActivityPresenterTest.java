package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.view.CityListActivityView;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CityListActivityPresenterTest {

    private final List<City> MANY_CITIES = Arrays.asList(new City(), new City(), new City());

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    SchedulerProvider mSchedulerProvider;

    @Mock
    CityListActivityView mCityListActivityView;

    @InjectMocks
    private CityListActivityPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        mPresenter.setSubscribeScheduler(Schedulers.trampoline());
        mPresenter.setObserveScheduler(Schedulers.trampoline());
        mPresenter.setView(mCityListActivityView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setViewShouldThrowExceptionWhenNullView() {
        mPresenter.setView(null);
    }

    @Test
    public void loadCitiesFromDatabaseShouldDisplayCitiesWhenNoEmptyList() {
        when(mDataManager.getCities()).thenReturn(Observable.just(MANY_CITIES));

        mPresenter.loadCitiesFromDatabase();

        verify(mCityListActivityView).displayCities(ArgumentMatchers.<City>anyList());
    }

    @Test
    public void loadCitiesFromDatabaseShouldShowNoDataWhenEmptyList() {
        when(mDataManager.getCities()).thenReturn(Observable.just(Collections.<City>emptyList()));

        mPresenter.loadCitiesFromDatabase();

        verify(mCityListActivityView).showNoData();
    }

    @Test
    public void loadCitiesFromDatabaseShouldShowLoadErrorInfoWhenException() {
        when(mDataManager.getCities()).thenReturn(Observable.<List<City>>error(new Throwable()));

        mPresenter.loadCitiesFromDatabase();

        verify(mCityListActivityView).showLoadErrorInfo();
    }

    @Test
    public void deleteCityFromDatabaseShouldReloadData() {
        when(mDataManager.removeCity(anyInt())).thenReturn(Completable.complete());

        mPresenter.deleteCityFromDatabase(anyInt());

        verify(mCityListActivityView).reloadData();
    }

    @Test
    public void deleteCityFromDatabaseShouldShowDeleteErrorInfoWhenException() {
        when(mDataManager.removeCity(anyInt())).thenReturn(Completable.error(new Throwable()));

        mPresenter.deleteCityFromDatabase(anyInt());

        verify(mCityListActivityView).showDeleteErrorInfo();
    }
}