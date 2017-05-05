package com.example.daniel.weatherinfo.application;

import android.app.Application;

import com.example.daniel.weatherinfo.database.Database;
import com.example.daniel.weatherinfo.database.DatabaseImpl;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class WeatherApplication extends Application {

    private static Database mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = OpenHelperManager.getHelper(this, DatabaseImpl.class);
    }

    public static Database getDatabase() {
        return mDatabase;
    }
}
