package com.example.daniel.weatherinfo.data.network;

import com.example.daniel.weatherinfo.data.network.model.ResponseByCity;
import com.example.daniel.weatherinfo.data.network.model.ResponseByIds;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @GET("weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<ResponseByCity> getWeatherByCity(@Query("q") String city);

    @GET("group?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<ResponseByIds> getWeatherByIds(@Query("id") String ids);

    class Factory {
        public static OpenWeatherMapService makeWeatherService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
            return retrofit.create(OpenWeatherMapService.class);
        }
    }
}
