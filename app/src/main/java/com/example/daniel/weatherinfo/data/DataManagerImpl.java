package com.example.daniel.weatherinfo.data;

import android.content.SharedPreferences;

import com.example.daniel.weatherinfo.data.database.Database;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
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
import io.reactivex.functions.Function;

@Singleton
public class DataManagerImpl implements DataManager {

    private static final String CURRENT_CITY_ID = "currentCityId";

    private final Database mDatabase;
    private final OpenWeatherMapService mOpenWeatherMapService;
    private final SharedPreferences mSharedPreferences;
    private final Mapper mMapper;

    @Inject
    public DataManagerImpl(Database database, OpenWeatherMapService openWeatherMapService, SharedPreferences sharedPreferences, Mapper mapper) {
        mDatabase = database;
        mOpenWeatherMapService = openWeatherMapService;
        mSharedPreferences = sharedPreferences;
        mMapper = mapper;
    }

    @Override
    public Observable<List<City>> getCitiesFromDatabase() {
        return Observable.fromCallable(new Callable<List<City>>() {
            @Override
            public List<City> call() throws Exception {
                return mDatabase.getCities();
            }
        });
    }

    @Override
    public Observable<City> getCityFromDatabase(final int cityId) {
        return Observable.fromCallable(new Callable<City>() {
            @Override
            public City call() throws Exception {
                return mDatabase.getCity(cityId);
            }
        });
    }

    @Override
    public Observable<City> getCityFromDatabase(final String cityName) {
        return Observable.fromCallable(new Callable<City>() {
            @Override
            public City call() throws Exception {
                return mDatabase.getCity(cityName);
            }
        });
    }

    @Override
    public Completable saveCityToDatabase(final City city) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.saveCity(city);
            }
        });
    }

    @Override
    public Completable removeCityFromDatabase(final int cityId) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.removeCity(cityId);
            }
        });
    }

    @Override
    public Observable<City> getCityFromNetwork(String apiKey, int cityId, String language) {
        return mOpenWeatherMapService
                .getCityWeatherDataById(apiKey, cityId, language)
                .map(new Function<CityWeatherData, City>() {
                    @Override
                    public City apply(CityWeatherData cityWeatherData) throws Exception {
                        return mMapper.mapCity(cityWeatherData);
                    }
                });
    }

    @Override
    public Observable<City> getCityFromNetwork(String apiKey, double lat, double lon, String language) {
        return mOpenWeatherMapService
                .getCityWeatherDataByCoordinates(apiKey, lat, lon, language)
                .map(new Function<CityWeatherData, City>() {
                    @Override
                    public City apply(CityWeatherData cityWeatherData) throws Exception {
                        return mMapper.mapCity(cityWeatherData);
                    }
                });
    }

    @Override
    public Observable<List<Forecast>> getForecastsFromNetwork(String apiKey, int cityId, String language) {
        return mOpenWeatherMapService
                .getCityForecastDataById(apiKey, cityId, language)
                .map(new Function<CityForecastData, List<Forecast>>() {
                    @Override
                    public List<Forecast> apply(CityForecastData cityForecastData) throws Exception {
                        return mMapper.mapForecast(cityForecastData);
                    }
                });
    }

    @Override
    public Observable<Integer> getCurrentCityId() {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mSharedPreferences.getInt(CURRENT_CITY_ID, 0);
            }
        });
    }

    @Override
    public Completable saveCurrentCityId(final int data) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mSharedPreferences.edit().putInt(CURRENT_CITY_ID, data).apply();
            }
        });
    }
}
