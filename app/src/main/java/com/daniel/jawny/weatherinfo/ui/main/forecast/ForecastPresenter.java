package com.daniel.jawny.weatherinfo.ui.main.forecast;

import com.daniel.jawny.weatherinfo.data.DataManager;
import com.daniel.jawny.weatherinfo.di.PerActivity;
import com.daniel.jawny.weatherinfo.ui.base.BasePresenter;
import com.daniel.jawny.weatherinfo.util.SchedulerProvider;

import javax.inject.Inject;

@PerActivity
public class ForecastPresenter extends BasePresenter<ForecastView> {

    @Inject
    public ForecastPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
}
