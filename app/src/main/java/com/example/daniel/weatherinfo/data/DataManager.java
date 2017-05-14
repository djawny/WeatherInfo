package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.data.database.Database;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.OpenWeatherMapService;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityName;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class DataManager implements DataManagerInterface {

    private static DataManager mInstance = new DataManager();

    private final Database mDatabase;
    private final OpenWeatherMapService mOpenWeatherMapService;

    public DataManager() {
        mDatabase = MyApplication.getDatabase();
        mOpenWeatherMapService = OpenWeatherMapService.Factory.makeApiService();
    }

    public static DataManager getInstance() {
        return mInstance;
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
    public Observable<Boolean> saveCities(final List<City> cities) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.saveCities(cities);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveCity(final City city) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.saveCity(city);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> removeCity(final int cityId) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.removeCity(cityId);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> removeCities() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.removeAllCities();
                return true;
            }
        });
    }

    @Override
    public Observable<WeatherDataByCityName> getWeatherDataByCityName(String cityName) {
        return mOpenWeatherMapService.getWeatherDataByCityName(cityName);
    }

    @Override
    public Observable<WeatherDataByCityIds> getWeatherDataByCityIds(String cityIds) {
        return mOpenWeatherMapService.getWeatherDataByCityIds(cityIds);
    }
}
