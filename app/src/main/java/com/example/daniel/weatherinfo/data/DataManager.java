package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;
import com.example.daniel.weatherinfo.data.network.model.CitiesWeatherData;

import java.util.List;

import io.reactivex.Observable;

public interface DataManager {

    Observable<List<City>> getCities();

    Observable<City> getCity(int cityId);

    Observable<Boolean> saveCities(List<City> cities);

    Observable<Boolean> saveCity(City city);

    Observable<Boolean> removeCity(int cityId);

    Observable<Boolean> removeCities();

    Observable<CityWeatherData> getCityWeatherDataByName(String cityName);

    Observable<CityWeatherData> getCityWeatherDataById(int cityId);

    Observable<CitiesWeatherData> getCitiesWeatherDataByIds(String cityIds);

    Observable<CityForecastData> getCityForecastDataById(int cityId);
}
