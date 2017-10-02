package com.example.daniel.weatherinfo.data;

import android.content.SharedPreferences;

import com.example.daniel.weatherinfo.data.database.Database;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.OpenWeatherMapService;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

@Singleton
public class DataManagerImpl implements DataManager {

    private final Database mDatabase;
    private final OpenWeatherMapService mOpenWeatherMapService;
    private final SharedPreferences mSharedPreferences;

    @Inject
    public DataManagerImpl(Database database, OpenWeatherMapService openWeatherMapService, SharedPreferences sharedPreferences) {
        mDatabase = database;
        mOpenWeatherMapService = openWeatherMapService;
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public Observable<List<City>> getCities() {
        return Observable.fromCallable(new Callable<List<City>>() {
            @Override
            public List<City> call() throws Exception {
                return mDatabase.getCities();
            }
        });
    }

    @Override
    public Observable<City> getCity(final int cityId) {
        return Observable.fromCallable(new Callable<City>() {
            @Override
            public City call() throws Exception {
                return mDatabase.getCity(cityId);
            }
        });
    }

    @Override
    public Observable<City> getCity(final String cityName) {
        return Observable.fromCallable(new Callable<City>() {
            @Override
            public City call() throws Exception {
                return mDatabase.getCity(cityName);
            }
        });
    }

    @Override
    public Completable saveCity(final City city) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.saveCity(city);
            }
        });
    }

    @Override
    public Completable removeCity(final int cityId) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.removeCity(cityId);
            }
        });
    }

    @Override
    public Observable<CityWeatherData> getCityWeatherDataById(String apiKey, int cityId, String language) {
        return mOpenWeatherMapService.getCityWeatherDataById(apiKey, cityId, language);
    }

    @Override
    public Observable<CityWeatherData> getCityWeatherDataByCoordinates(String apiKey, double lat, double lon, String language) {
        return mOpenWeatherMapService.getCityWeatherDataByCoordinates(apiKey, lat, lon, language);
    }

    @Override
    public Observable<CityForecastData> getCityForecastDataById(String apiKey, int cityId, String language) {
        return mOpenWeatherMapService.getCityForecastDataById(apiKey, cityId, language);
    }

    @Override
    public Completable putIntSharedPreferences(final String key, final int data) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mSharedPreferences.edit().putInt(key, data).apply();
            }
        });
    }

    @Override
    public Observable<Integer> getIntSharedPreferences(final String key) {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mSharedPreferences.getInt(key, 0);
            }
        });
    }
}
