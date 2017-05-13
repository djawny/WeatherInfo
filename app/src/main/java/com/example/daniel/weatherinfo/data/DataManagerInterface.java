package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityId;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;

import java.util.List;

import io.reactivex.Observable;

public interface DataManagerInterface {

    Observable<List<City>> getCities();

    Observable<City> getCity(int cityId);

    Observable<Boolean> saveCities(List<City> cities);

    Observable<Boolean> saveCity(City city);

    Observable<Boolean> removeCity(int cityId);

    Observable<Boolean> removeCities();

    Observable<WeatherDataByCityId> getWeatherDataByCityName(String cityName);

    Observable<WeatherDataByCityIds> getWeatherDataByCityIds(String cityIds);
}
