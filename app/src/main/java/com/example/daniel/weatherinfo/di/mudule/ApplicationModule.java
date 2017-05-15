package com.example.daniel.weatherinfo.di.mudule;

import android.content.Context;

import com.example.daniel.weatherinfo.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final MyApplication mApplication;

    public ApplicationModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public MyApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mApplication;
    }
}
