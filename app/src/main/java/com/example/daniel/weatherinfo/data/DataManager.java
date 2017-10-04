package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface DataManager {

    Observable<List<City>> getCitiesFromDatabase();

    Observable<City> getCityFromDatabase(int cityId);

    Observable<City> getCityFromDatabase(String cityName);

    Completable saveCityToDatabase(City city);

    Completable removeCityFromDatabase(int cityId);

    Observable<City> getCityFromNetwork(String apiKey, int cityId, String language);

    Observable<City> getCityFromNetwork(String apiKey, double lat, double lon, String language);

    Observable<List<Forecast>> getForecastsFromNetwork(String apiKey, int cityId, String language);

    Observable<Integer> getCurrentCityId();

    Completable saveCurrentCityId(int data);
}
