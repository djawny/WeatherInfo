package com.example.daniel.weatherinfo.di.mudule;

import android.app.Activity;
import android.content.Context;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.di.ActivityContext;
import com.example.daniel.weatherinfo.di.PerActivity;
import com.example.daniel.weatherinfo.ui.locations.LocationsPresenter;
import com.example.daniel.weatherinfo.ui.main.MainPresenter;
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
    @PerActivity
    MainPresenter provideMainActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new MainPresenter(dataManager, schedulerProvider);
    }

    @Provides
    @PerActivity
    LocationsPresenter provideCityListActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new LocationsPresenter(dataManager, schedulerProvider);
    }
}
