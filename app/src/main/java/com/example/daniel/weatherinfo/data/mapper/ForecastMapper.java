package com.example.daniel.weatherinfo.data.mapper;

import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.network.model.CityForecast;

public class ForecastMapper {
    public static Forecast map(CityForecast cityForecast) {
        Forecast forecast = new Forecast();
        forecast.setDate(cityForecast.getDate());
        forecast.setDateTxt(cityForecast.getDateTxt());
        forecast.setTemp(cityForecast.getMain().getTemp());
        forecast.setIcon(cityForecast.getWeather().get(0).getIcon());
        return forecast;
    }
}
