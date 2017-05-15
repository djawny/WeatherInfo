package com.example.daniel.weatherinfo.di.mudule;

import android.app.Activity;

import com.example.daniel.weatherinfo.di.PerActivity;
import com.example.daniel.weatherinfo.ui.AddCityActivityPresenter;
import com.example.daniel.weatherinfo.ui.MainActivityPresenter;
import com.example.daniel.weatherinfo.ui.PageFragmentPresenter;
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
    SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProviderImpl();
    }

    @PerActivity
    @Provides
    MainActivityPresenter provideMainActivityPresenter(SchedulerProvider schedulerProvider) {
        return new MainActivityPresenter(schedulerProvider);
    }

    @PerActivity
    @Provides
    AddCityActivityPresenter provideAddCityActivityPresenter(SchedulerProvider schedulerProvider) {
        return new AddCityActivityPresenter(schedulerProvider);
    }

    @PerActivity
    @Provides
    PageFragmentPresenter providePageFragmentPresenter(SchedulerProvider schedulerProvider) {
        return new PageFragmentPresenter(schedulerProvider);
    }
}
