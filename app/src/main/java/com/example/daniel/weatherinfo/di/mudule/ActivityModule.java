package com.example.daniel.weatherinfo.di.mudule;

import android.app.Activity;
import android.content.Context;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.di.ActivityContext;
import com.example.daniel.weatherinfo.di.PerActivity;
import com.example.daniel.weatherinfo.ui.presenter.AddCityActivityPresenter;
import com.example.daniel.weatherinfo.ui.presenter.CityListActivityPresenter;
import com.example.daniel.weatherinfo.ui.presenter.MainActivityPresenter;
import com.example.daniel.weatherinfo.util.SchedulerProvider;
import com.example.daniel.weatherinfo.util.SchedulerProviderImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProviderImpl();
    }

    @Provides
    Mapper provideMapper() {
        return new Mapper();
    }

    @PerActivity
    @Provides
    MainActivityPresenter provideMainActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, Mapper mapper) {
        return new MainActivityPresenter(dataManager, schedulerProvider, mapper);
    }

    @PerActivity
    @Provides
    CityListActivityPresenter provideCityListActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new CityListActivityPresenter(dataManager, schedulerProvider);
    }

    @PerActivity
    @Provides
    AddCityActivityPresenter provideAddCityActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, Mapper mapper) {
        return new AddCityActivityPresenter(dataManager, schedulerProvider, mapper);
    }
}
