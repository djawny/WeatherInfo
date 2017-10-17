package com.daniel.jawny.weatherinfo.data.network;

import com.daniel.jawny.weatherinfo.data.network.model.CityWeatherForecastsResponse;
import com.daniel.jawny.weatherinfo.data.network.model.CityWeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    @GET("weather?units=metric")
    Observable<CityWeatherResponse> getCityWeatherById(
            @Query("appid") String apiKey,
            @Query("id") int cityId,
            @Query("lang") String language);

    @GET("weather?units=metric")
    Observable<CityWeatherResponse> getCityWeatherByCoordinates(
            @Query("appid") String apiKey,
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("lang") String language);

    @GET("forecast?units=metric")
    Observable<CityWeatherForecastsResponse> getCityWeatherForecastsById(
            @Query("appid") String apiKey,
            @Query("id") int cityId,
            @Query("lang") String language);
}
