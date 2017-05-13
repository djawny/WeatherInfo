package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.application.AndroidApplication;
import com.example.daniel.weatherinfo.data.database.Database;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.OpenWeatherMapService;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityId;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class CityDataManager implements CityDataManagerInterface {

    private static CityDataManager mInstance = new CityDataManager();

    private final Database mDatabase;
    private final OpenWeatherMapService mOpenWeatherMapService;

    public CityDataManager() {
        mDatabase = AndroidApplication.getDatabase();
        mOpenWeatherMapService = OpenWeatherMapService.Factory.makeApiService();
    }

    public static CityDataManager getInstance() {
        return mInstance;
    }

    @Override
    public Observable<List<City>> getCitiesFromDB() {
        return Observable.fromCallable(new Callable<List<City>>() {
            @Override
            public List<City> call() throws Exception {
                return mDatabase.getCities();
            }
        });
    }

    @Override
    public Observable<City> getCityFromDB(final int cityId) {
        return Observable.fromCallable(new Callable<City>() {
            @Override
            public City call() throws Exception {
                return mDatabase.getCity(cityId);
            }
        });
    }

    @Override
    public Observable<Boolean> saveCitiesToDB(final List<City> cities) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.saveCities(cities);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveCityToDB(final City city) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.saveCity(city);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> removeCityFromDB(final int cityId) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.removeCity(cityId);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> removeCitiesFromDB() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDatabase.removeAllCities();
                return true;
            }
        });
    }

    @Override
    public Observable<WeatherDataByCityId> getApiResponseByCityId(String cityName) {
        return mOpenWeatherMapService.getWeatherDataByCityName(cityName);
    }

    @Override
    public Observable<WeatherDataByCityIds> getApiResponseByCityIds(String cityIds) {
        return mOpenWeatherMapService.getWeatherDataByCityIds(cityIds);
    }
}
