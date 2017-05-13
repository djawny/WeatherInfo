package com.example.daniel.weatherinfo.data;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityId;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;

import java.util.List;

import io.reactivex.Observable;

public interface CityDataManagerInterface {

    Observable<List<City>> getCitiesFromDB();

    Observable<City> getCityFromDB(int cityId);

    Observable<Boolean> saveCitiesToDB(List<City> cities);

    Observable<Boolean> saveCityToDB(City city);

    Observable<Boolean> removeCityFromDB(int cityId);

    Observable<Boolean> removeCitiesFromDB();

    Observable<WeatherDataByCityId> getApiResponseByCityId(String cityName);

    Observable<WeatherDataByCityIds> getApiResponseByCityIds(String cityIds);
}
