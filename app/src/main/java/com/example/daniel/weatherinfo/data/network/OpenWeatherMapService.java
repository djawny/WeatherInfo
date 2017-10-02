package com.example.daniel.weatherinfo.data.network;

import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    @GET("weather?units=metric")
    Observable<CityWeatherData> getCityWeatherDataById(@Query("appid") String apiKey, @Query("id") int cityId, @Query("lang") String language);

    @GET("weather?units=metric")
    Observable<CityWeatherData> getCityWeatherDataByCoordinates(@Query("appid") String apiKey, @Query("lat") double lat, @Query("lon") double lon, @Query("lang") String language);

    @GET("forecast?units=metric")
    Observable<CityForecastData> getCityForecastDataById(@Query("appid") String apiKey, @Query("id") int cityId, @Query("lang") String language);
}
