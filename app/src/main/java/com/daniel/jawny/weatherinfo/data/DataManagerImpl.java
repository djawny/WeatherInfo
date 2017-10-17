package com.daniel.jawny.weatherinfo.data;

import com.daniel.jawny.weatherinfo.data.database.Database;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.daniel.jawny.weatherinfo.data.mapper.CityMapper;
import com.daniel.jawny.weatherinfo.data.mapper.ForecastsMapper;
import com.daniel.jawny.weatherinfo.data.network.OpenWeatherMapService;
import com.daniel.jawny.weatherinfo.data.prefs.Preferences;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Singleton
public class DataManagerImpl implements DataManager {

    private static final String CURRENT_CITY_ID = "currentCityId";

    private final Database mDatabase;
    private final OpenWeatherMapService mOpenWeatherMapService;
    private final Preferences mPreferences;

    @Inject
    public DataManagerImpl(Database database, OpenWeatherMapService openWeatherMapService, Preferences preferences) {
        mDatabase = database;
        mOpenWeatherMapService = openWeatherMapService;
        mPreferences = preferences;
    }

    @Override
    public Observable<List<City>> getCitiesFromDatabase() {
        return Observable.fromCallable(mDatabase::getCities);
    }

    @Override
    public Observable<City> getCityFromDatabase(final int cityId) {
        return Observable.fromCallable(() -> mDatabase.getCity(cityId));
    }

    @Override
    public Observable<City> getCityFromDatabase(final String cityName) {
        return Observable.fromCallable(() -> mDatabase.getCity(cityName));
    }

    @Override
    public Completable saveCityToDatabase(final City city) {
        return Completable.fromAction(() -> mDatabase.saveCity(city));
    }

    @Override
    public Completable removeCityFromDatabase(final int cityId) {
        return Completable.fromAction(() -> mDatabase.removeCity(cityId));
    }

    @Override
    public Observable<City> getCityFromNetwork(String apiKey, int cityId, String language) {
        return mOpenWeatherMapService
                .getCityWeatherById(apiKey, cityId, language)
                .map(CityMapper::map);
    }

    @Override
    public Observable<City> getCityFromNetwork(String apiKey, double lat, double lon, String language) {
        return mOpenWeatherMapService
                .getCityWeatherByCoordinates(apiKey, lat, lon, language)
                .map(CityMapper::map);
    }

    @Override
    public Observable<List<Forecast>> getForecastsFromNetwork(String apiKey, int cityId, String language) {
        return mOpenWeatherMapService
                .getCityWeatherForecastsById(apiKey, cityId, language)
                .map(ForecastsMapper::map);
    }

    @Override
    public Observable<Integer> getCurrentCityId() {
        return Observable.fromCallable(() -> mPreferences.get(CURRENT_CITY_ID, 0));
    }

    @Override
    public Completable saveCurrentCityId(final int data) {
        return Completable.fromAction(() -> mPreferences.put(CURRENT_CITY_ID, data));
    }
}
