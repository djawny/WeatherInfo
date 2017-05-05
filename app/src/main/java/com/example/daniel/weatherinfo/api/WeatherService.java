package com.example.daniel.weatherinfo.api;

import com.example.daniel.weatherinfo.model.api.WeatherData;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";
//    String API_KEY = "779bcb1c99f4dcd8ffe6b596d5dc919d";

    @GET("weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric&lang=pl")
    Observable<WeatherData> getWeatherByCity(@Query("q") String city);

    @GET("weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric&lang=pl")
    Observable<WeatherData> getWeatherById(@Query("id") int id);

    class Factory {
        public static WeatherService makeWeatherService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
            return retrofit.create(WeatherService.class);
        }
    }
}
