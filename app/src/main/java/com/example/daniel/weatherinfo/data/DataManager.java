package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface DataManager {

    Observable<List<City>> getCities();

    Observable<City> getCity(int cityId);

    Observable<City> getCity(String cityName);

//    Completable saveCities(List<City> cities);

    Completable saveCity(City city);

    Completable removeCity(int cityId);

//    Completable removeCities();

    Observable<CityWeatherData> getCityWeatherDataByName(String apiKey, String cityName);

    Observable<CityWeatherData> getCityWeatherDataById(String apiKey, int cityId);

    Observable<CityWeatherData> getCityWeatherDataByCoordinates(String apiKey, double lat, double lon);

    Observable<CityForecastData> getCityForecastDataById(String apiKey, int cityId);

//    Observable<CitiesWeatherData> getCitiesWeatherDataByIds(String apiKey, String cityIds);

    Completable putIntSharedPreferences(String key, int data);

    Observable<Integer> getIntSharedPreferences(String key);
}
