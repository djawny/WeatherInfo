package com.example.daniel.weatherinfo.repository;

import com.example.daniel.weatherinfo.model.City;

import java.util.List;

import io.reactivex.Observable;

public interface CityRepositoryInterface {

    Observable<List<City>> getCitiesRx();

    Observable<City> getCityRx(int cityId);

    Observable<Void> saveCitiesRx(List<City> cities);

    Observable<Void> saveCityRx(City city);

    Observable<Void> removeCityRx(int cityId);

    Observable<Void> removeAllCitiesRx();
}
