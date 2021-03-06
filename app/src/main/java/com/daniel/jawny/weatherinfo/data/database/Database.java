package com.daniel.jawny.weatherinfo.data.database;

import com.daniel.jawny.weatherinfo.data.database.model.City;

import java.util.List;

public interface Database {

    List<City> getCities();

    City getCity(int cityId);

    City getCity(String cityName);

    void saveCity(City city);

    void removeCity(int cityId);
}
