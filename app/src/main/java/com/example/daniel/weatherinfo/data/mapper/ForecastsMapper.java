package com.example.daniel.weatherinfo.data.mapper;

import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherForecastsResponse;
import com.example.daniel.weatherinfo.data.network.model.ForecastEntity;

import java.util.ArrayList;
import java.util.List;

public class ForecastsMapper {
    public static List<Forecast> map(CityWeatherForecastsResponse cityWeatherForecastsResponse) {
        List<ForecastEntity> forecastEntityList = cityWeatherForecastsResponse.getList();
        ArrayList<Forecast> forecasts = new ArrayList<>();
        for (ForecastEntity forecastEntity : forecastEntityList) {
            Forecast forecast = new Forecast();
            forecast.setDate(forecastEntity.getDate());
            forecast.setDateTxt(forecastEntity.getDateTxt());
            forecast.setTemp(forecastEntity.getMain().getTemp());
            forecast.setIcon(forecastEntity.getWeather().get(0).getIcon());
            forecasts.add(forecast);
        }
        return forecasts;
    }
}
