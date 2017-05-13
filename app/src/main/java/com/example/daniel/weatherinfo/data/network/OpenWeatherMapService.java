package com.example.daniel.weatherinfo.data.network;

import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityName;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.daniel.weatherinfo.util.AppConstants.BASE_URL;

public interface OpenWeatherMapService {

    @GET("weather?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<WeatherDataByCityName> getWeatherDataByCityName(@Query("q") String cityName);

    @GET("group?appid=779bcb1c99f4dcd8ffe6b596d5dc919d&units=metric")
    Observable<WeatherDataByCityIds> getWeatherDataByCityIds(@Query("id") String cityIds);

    class Factory {
        public static OpenWeatherMapService makeApiService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
            return retrofit.create(OpenWeatherMapService.class);
        }
    }
}
