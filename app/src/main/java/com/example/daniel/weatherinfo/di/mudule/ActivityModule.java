package com.example.daniel.weatherinfo.di.mudule;

import android.app.Activity;

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

    @Provides
    MainActivityPresenter provideMainActivityPresenter() {
        return null;
    }

    @Provides
    AddCityActivityPresenter provideAddCityActivityPresenter() {
        return null;
    }

    @Provides
    PageFragmentPresenter providePageFragmentPresenter() {
        return null;
    }
}
