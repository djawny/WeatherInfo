package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.application.AndroidApplication;
import com.example.daniel.weatherinfo.data.database.Database;
import com.example.daniel.weatherinfo.data.database.model.City;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class CityRepository implements CityRepositoryInterface {

    private static CityRepository mInstance = new CityRepository();

    private final Database mDatabase;

    public CityRepository() {
        mDatabase = AndroidApplication.getDatabase();
    }

    public static CityRepository getInstance() {
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
    public Observable<Void> saveCitiesRx(final List<City> cities) {
        return Observable.defer(new Callable<ObservableSource<? extends Void>>() {
            @Override
            public ObservableSource<? extends Void> call() throws Exception {
                mDatabase.saveCities(cities);
                return Observable.empty();
            }
        });
    }

    @Override
    public Observable<Void> saveCityRx(final City city) {
        return Observable.defer(new Callable<ObservableSource<? extends Void>>() {
            @Override
            public ObservableSource<? extends Void> call() throws Exception {
                mDatabase.saveCity(city);
                return Observable.empty();
            }
        });
    }

    @Override
    public Observable<Void> removeCityRx(final int cityId) {
        return Observable.defer(new Callable<ObservableSource<? extends Void>>() {
            @Override
            public ObservableSource<? extends Void> call() throws Exception {
                mDatabase.removeCity(cityId);
                return Observable.empty();
            }
        });
    }

    @Override
    public Observable<Void> removeAllCitiesRx() {
        return Observable.defer(new Callable<ObservableSource<? extends Void>>() {
            @Override
            public ObservableSource<? extends Void> call() throws Exception {
                mDatabase.removeAllCities();
                return Observable.empty();
            }
        });
    }
}
