package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.data.database.Database;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.OpenWeatherMapService;
import com.example.daniel.weatherinfo.data.network.model.CitiesWeatherData;
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

    @Inject
    public DataManagerImpl(Database database, OpenWeatherMapService openWeatherMapService) {
        mDatabase = database;
        mOpenWeatherMapService = openWeatherMapService;
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
    public Completable saveCities(final List<City> cities) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.saveCities(cities);
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
    public Completable removeCities() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.removeAllCities();
            }
        });
    }

    @Override
    public Observable<CityWeatherData> getCityWeatherDataByName(String apiKey, String cityName) {
        return mOpenWeatherMapService.getCityWeatherDataByName(apiKey, cityName);
    }

    @Override
    public Observable<CityWeatherData> getCityWeatherDataById(String apiKey, int cityId) {
        return mOpenWeatherMapService.getCityWeatherDataById(apiKey, cityId);
    }

    @Override
    public Observable<CitiesWeatherData> getCitiesWeatherDataByIds(String apiKey, String cityIds) {
        return mOpenWeatherMapService.getCitiesWeatherDataByIds(apiKey, cityIds);
    }

    @Override
    public Observable<CityForecastData> getCityForecastDataById(String apiKey, int cityId) {
        return mOpenWeatherMapService.getCityForecastDataById(apiKey, cityId);
    }
}
