package com.example.daniel.weatherinfo.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daniel.weatherinfo.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PreferencesImpl implements Preferences {

    private final SharedPreferences mSharedPreferences;

    @Inject
    public PreferencesImpl(@ApplicationContext Context context, @Named("preferencesName") String preferencesName) {
        mSharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

    @Override
    public void put(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    @Override
    public void put(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    @Override
    public void put(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    @Override
    public void put(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    @Override
    public Integer get(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    @Override
    public Float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    @Override
    public Boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    @Override
    public void deleteSavedData(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }
}
