package com.example.daniel.weatherinfo.data.mapper;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityName;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;
import com.example.daniel.weatherinfo.data.database.model.Weather;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public Mapper() {
    }

    public static City mapCity(WeatherDataByCityName weatherData) {
        City city = new City();
        if (weatherData != null) {
            city.setId(weatherData.getId());
            city.setName(weatherData.getName());
            city.setCountry(weatherData.getSys().getCountry());
            Weather weather = new Weather();
            weather.setId(weatherData.getId());
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

    public static List<City> mapCities(WeatherDataByCityIds weatherDataByCityIds) {
        ArrayList<City> cities = new ArrayList<>();
        if (weatherDataByCityIds != null) {
            List<WeatherDataByCityName> weatherDataList = weatherDataByCityIds.getList();
            for (WeatherDataByCityName weatherData : weatherDataList) {
                City city = mapCity(weatherData);
                cities.add(city);
            }
        }
        return cities;
    }
}
