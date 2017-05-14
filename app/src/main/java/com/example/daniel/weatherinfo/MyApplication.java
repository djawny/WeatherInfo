package com.example.daniel.weatherinfo;

import android.app.Application;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.di.component.ApplicationComponent;
import com.example.daniel.weatherinfo.di.component.DaggerApplicationComponent;
import com.example.daniel.weatherinfo.di.mudule.ApplicationModule;

import javax.inject.Inject;

import static com.example.daniel.weatherinfo.util.AppConstants.BASE_URL;

public class MyApplication extends Application {

    @Inject
    DataManager mDataManager;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeApplicationComponent();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void initializeApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this, BASE_URL))
                .build();
    }
}
