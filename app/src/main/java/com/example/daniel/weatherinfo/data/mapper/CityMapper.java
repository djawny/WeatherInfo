package com.example.daniel.weatherinfo.data.mapper;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Weather;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherResponse;

public class CityMapper {
    public static City map(CityWeatherResponse cityWeatherResponse) {
        City city = new City();
        city.setId(cityWeatherResponse.getId());
        city.setName(cityWeatherResponse.getName());
        city.setLongitude(cityWeatherResponse.getCoordinates().getLon());
        city.setLatitude(cityWeatherResponse.getCoordinates().getLat());
        city.setCountry(cityWeatherResponse.getSys().getCountry());
        Weather weather = new Weather();
        weather.setTemp(cityWeatherResponse.getMain().getTemp());
        weather.setTempMin(cityWeatherResponse.getMain().getTempMin());
        weather.setTempMax(cityWeatherResponse.getMain().getTempMax());
        weather.setHumidity(cityWeatherResponse.getMain().getHumidity());
        weather.setCloudiness(cityWeatherResponse.getClouds().getCloudiness());
        weather.setWindSpeed(cityWeatherResponse.getWind().getSpeed());
        weather.setWindDegree(cityWeatherResponse.getWind().getDegree());
        weather.setPressure(cityWeatherResponse.getMain().getPressure());
        weather.setDescription(cityWeatherResponse.getWeather().get(0).getDescription());
        weather.setIcon(cityWeatherResponse.getWeather().get(0).getIcon());
        weather.setDate(cityWeatherResponse.getDate());
        weather.setSunrise(cityWeatherResponse.getSys().getSunrise());
        weather.setSunset(cityWeatherResponse.getSys().getSunset());
        city.setWeather(weather);
        return city;
    }
}
