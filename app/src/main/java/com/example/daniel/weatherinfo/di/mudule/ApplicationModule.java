package com.example.daniel.weatherinfo.di.mudule;

import android.content.Context;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.di.ApplicationContext;

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
    public MyApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return mApplication;
    }
}
