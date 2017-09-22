package com.example.daniel.weatherinfo.data.network;

import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    @GET("weather?units=metric")
    Observable<CityWeatherData> getCityWeatherDataByName(@Query("appid") String apiKey, @Query("q") String cityName);

    @GET("weather?units=metric")
    Observable<CityWeatherData> getCityWeatherDataById(@Query("appid") String apiKey, @Query("id") int cityId);

    @GET("forecast?units=metric")
    Observable<CityForecastData> getCityForecastDataById(@Query("appid") String apiKey, @Query("id") int cityId);

//    @GET("group?units=metric")
//    Observable<CitiesWeatherData> getCitiesWeatherDataByIds(@Query("appid") String apiKey, @Query("id") String cityIds);
}
