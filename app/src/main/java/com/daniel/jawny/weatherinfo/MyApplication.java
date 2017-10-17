package com.daniel.jawny.weatherinfo;

import android.app.Application;
import android.content.Context;

import com.daniel.jawny.weatherinfo.di.component.ApplicationComponent;

import timber.log.Timber;

public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTimber();
        createApplicationComponent();
    }

    private void initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        }
    }

    public static ApplicationComponent getComponent(Context context) {
        return ((MyApplication) context.getApplicationContext()).mApplicationComponent;
    }

    private void createApplicationComponent() {
        mApplicationComponent = ApplicationComponent.Initializer.init(this);
        mApplicationComponent.inject(this);
    }
}
