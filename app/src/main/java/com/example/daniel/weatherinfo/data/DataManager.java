package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherData;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataList;

import java.util.List;

import io.reactivex.Observable;

public interface DataManager {

    Observable<List<City>> getCities();

    Observable<City> getCity(int cityId);

    Observable<Boolean> saveCities(List<City> cities);

    Observable<Boolean> saveCity(City city);

    Observable<Boolean> removeCity(int cityId);

    Observable<Boolean> removeCities();

    Observable<WeatherData> getWeatherDataByCityName(String cityName);

    Observable<WeatherDataList> getWeatherDataByCityIds(String cityIds);
}
