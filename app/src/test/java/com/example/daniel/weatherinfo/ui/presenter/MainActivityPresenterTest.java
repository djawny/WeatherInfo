package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class MainActivityPresenterTest {

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule();

    @Mock
    DataManager mDataManager;

    @Mock
    SchedulerProvider mSchedulerProvider;

    MainActivityPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        mPresenter = new MainActivityPresenter(mDataManager, mSchedulerProvider);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullView(){
        mPresenter.setView(null);
    }



}