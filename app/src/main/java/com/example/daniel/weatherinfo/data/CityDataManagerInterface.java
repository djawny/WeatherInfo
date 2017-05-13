package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.data.database.model.City;

import java.util.List;

import io.reactivex.Observable;

public interface CityDataManagerInterface {

    Observable<List<City>> getCitiesRx();

    Observable<City> getCityRx(int cityId);

    Observable<Boolean> saveCitiesRx(List<City> cities);

    Observable<Boolean> saveCityRx(City city);

    Observable<Boolean> removeCityRx(int cityId);

    Observable<Boolean> removeAllCitiesRx();
}
