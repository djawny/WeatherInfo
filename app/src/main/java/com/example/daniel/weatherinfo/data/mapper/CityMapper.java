package com.example.daniel.weatherinfo.data.mapper;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Weather;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;

public class CityMapper {
    public static City map(CityWeatherData cityWeatherData) {
        City city = new City();
        if (cityWeatherData != null) {
            city.setId(cityWeatherData.getId());
            city.setName(cityWeatherData.getName());
            city.setLongitude(cityWeatherData.getCoordinates().getLon());
            city.setLatitude(cityWeatherData.getCoordinates().getLat());
            city.setCountry(cityWeatherData.getSys().getCountry());
            Weather weather = new Weather();
            weather.setTemp(cityWeatherData.getMain().getTemp());
            weather.setTempMin(cityWeatherData.getMain().getTempMin());
            weather.setTempMax(cityWeatherData.getMain().getTempMax());
            weather.setHumidity(cityWeatherData.getMain().getHumidity());
            weather.setCloudiness(cityWeatherData.getClouds().getCloudiness());
            weather.setWindSpeed(cityWeatherData.getWind().getSpeed());
            weather.setWindDegree(cityWeatherData.getWind().getDegree());
            weather.setPressure(cityWeatherData.getMain().getPressure());
            weather.setDescription(cityWeatherData.getWeather().get(0).getDescription());
            weather.setIcon(cityWeatherData.getWeather().get(0).getIcon());
            weather.setDate(cityWeatherData.getDate());
            weather.setSunrise(cityWeatherData.getSys().getSunrise());
            weather.setSunset(cityWeatherData.getSys().getSunset());
            city.setWeather(weather);
        }
        return city;
    }
}
