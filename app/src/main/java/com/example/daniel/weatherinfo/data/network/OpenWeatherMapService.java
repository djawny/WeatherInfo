package com.example.daniel.weatherinfo.data.network;

import com.example.daniel.weatherinfo.data.network.model.ForecastDataByCityId;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityName;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    @GET("weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<WeatherDataByCityName> getWeatherDataByCityName(@Query("q") String cityName);

    @GET("group?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<WeatherDataByCityIds> getWeatherDataByCityIds(@Query("id") String cityIds);

    @GET("forecast?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<ForecastDataByCityId> getForecastDataByCityId(@Query("id") int cityId);
}
