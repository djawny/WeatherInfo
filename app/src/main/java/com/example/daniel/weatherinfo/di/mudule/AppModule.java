package com.example.daniel.weatherinfo.di.mudule;

import android.content.Context;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.di.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    MyApplication mApplication;

    public AppModule(MyApplication mApplication) {
        this.mApplication = mApplication;
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
