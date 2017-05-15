package com.example.daniel.weatherinfo;

import android.app.Application;
import android.content.Context;

import com.example.daniel.weatherinfo.di.component.ApplicationComponent;
import com.example.daniel.weatherinfo.di.component.DaggerApplicationComponent;
import com.example.daniel.weatherinfo.di.mudule.ApplicationModule;
import com.example.daniel.weatherinfo.di.mudule.DataManagerModule;

import static com.example.daniel.weatherinfo.util.AppConstants.BASE_URL;

public class MyApplication extends Application {

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

    public static ApplicationComponent getComponent(Context context) {
        return ((MyApplication) context.getApplicationContext()).mApplicationComponent;
    }

    private void initializeApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .dataManagerModule(new DataManagerModule(BASE_URL))
                .build();
    }
}
