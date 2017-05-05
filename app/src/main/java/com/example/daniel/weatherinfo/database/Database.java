package com.example.daniel.weatherinfo.database;

import com.example.daniel.weatherinfo.model.City;

import java.util.List;

public interface Database {

    List<City> getCities();

    City getCity(int cityId);

    void saveCities(List<City> cities);

    void saveCity(City city);

    void removeCity(int cityId);
}
