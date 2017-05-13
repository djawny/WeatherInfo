package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.application.AndroidApplication;
import com.example.daniel.weatherinfo.data.database.Database;
import com.example.daniel.weatherinfo.data.database.model.City;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class CityDataManager implements CityDataManagerInterface {

    private static CityDataManager mInstance = new CityDataManager();

    private final Database mDatabase;

    public CityDataManager() {
        mDatabase = AndroidApplication.getDatabase();
    }

    public static CityDataManager getInstance() {
        return mInstance;
    }

    @Override
    public Observable<List<City>> getCitiesRx() {
        return Observable.fromCallable(new Callable<List<City>>() {
            @Override
            public List<City> call() throws Exception {
                return mDatabase.getCities();
            }
        });
    }

    @Override
    public Observable<City> getCityRx(final int cityId) {
        return Observable.fromCallable(new Callable<City>() {
            @Override
            public City call() throws Exception {
                return mDatabase.getCity(cityId);
            }
        });
    }

    @Override
    public Observable<Boolean> saveCitiesRx(final List<City> cities) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.saveCities(cities);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveCityRx(final City city) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.saveCity(city);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> removeCityRx(final int cityId) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.removeCity(cityId);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> removeAllCitiesRx() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.removeAllCities();
                return true;
            }
        });
    }
}
