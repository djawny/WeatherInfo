package com.example.daniel.weatherinfo.data.mapper;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherData;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataList;
import com.example.daniel.weatherinfo.data.database.model.Weather;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public Mapper() {
    }

    public static City mapCity(WeatherData weatherData) {
        City city = new City();
        if (weatherData != null) {
            city.setId(weatherData.getId());
            city.setName(weatherData.getName());
            city.setCountry(weatherData.getSys().getCountry());
            Weather weather = new Weather();
            weather.setTemp(weatherData.getMain().getTemp());
            weather.setTempMin(weatherData.getMain().getTempMin());
            weather.setTempMax(weatherData.getMain().getTempMax());
            weather.setHumidity(weatherData.getMain().getHumidity());
            weather.setCloudiness(weatherData.getClouds().getCloudiness());
            weather.setWindSpeed(weatherData.getWind().getSpeed());
            weather.setWindDegree(weatherData.getWind().getDegree());
            weather.setPressure(weatherData.getMain().getPressure());
            weather.setDescription(weatherData.getWeather().get(0).getDescription());
            weather.setIcon(weatherData.getWeather().get(0).getIcon());
            weather.setDate(weatherData.getDate());
            weather.setSunrise(weatherData.getSys().getSunrise());
            weather.setSunset(weatherData.getSys().getSunset());
            city.setWeather(weather);
        }
        return city;
    }

    public static List<City> mapCities(WeatherDataList weatherDataList) {
        ArrayList<City> cities = new ArrayList<>();
        if (weatherDataList != null) {
            List<WeatherData> dataList = weatherDataList.getList();
            for (WeatherData weatherData : dataList) {
                City city = mapCity(weatherData);
                cities.add(city);
            }
        }
        return cities;
    }
}
