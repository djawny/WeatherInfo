package com.daniel.jawny.weatherinfo.di.mudule;

import android.app.Activity;
import android.content.Context;

import com.daniel.jawny.weatherinfo.di.ActivityContext;
import com.daniel.jawny.weatherinfo.util.SchedulerProvider;
import com.daniel.jawny.weatherinfo.util.SchedulerProviderImpl;

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
}
