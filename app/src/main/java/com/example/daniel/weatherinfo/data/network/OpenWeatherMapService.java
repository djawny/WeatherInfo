package com.example.daniel.weatherinfo.data.network;

import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CitiesWeatherData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    @GET("weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<CityWeatherData> getCityWeatherDataByName(@Query("q") String cityName);

    @GET("weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<CityWeatherData> getCityWeatherDataById(@Query("id") int cityId);

    @GET("group?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<CitiesWeatherData> getCitiesWeatherDataByIds(@Query("id") String cityIds);

    @GET("forecast?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<CityForecastData> getCityForecastDataById(@Query("id") int cityId);
}
