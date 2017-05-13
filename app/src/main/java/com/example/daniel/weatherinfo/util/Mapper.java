package com.example.daniel.weatherinfo.util;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityId;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;
import com.example.daniel.weatherinfo.data.database.model.Weather;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public Mapper() {
    }

    public static City mapCity(WeatherDataByCityId weatherDataByCityId) {
        City city = new City();
        if (weatherDataByCityId != null) {
            city.setId(weatherDataByCityId.getId());
            city.setName(weatherDataByCityId.getName());
            city.setCountry(weatherDataByCityId.getSys().getCountry());
            Weather weather = new Weather();
            weather.setId(weatherDataByCityId.getId());
            weather.setTemp(weatherDataByCityId.getMain().getTemp());
            weather.setTempMin(weatherDataByCityId.getMain().getTempMin());
            weather.setTempMax(weatherDataByCityId.getMain().getTempMax());
            weather.setHumidity(weatherDataByCityId.getMain().getHumidity());
            weather.setCloudiness(weatherDataByCityId.getClouds().getAll());
            weather.setWindSpeed(weatherDataByCityId.getWind().getSpeed());
            weather.setWindDegree(weatherDataByCityId.getWind().getDeg());
            weather.setPressure(weatherDataByCityId.getMain().getPressure());
            weather.setDescription(weatherDataByCityId.getWeather().get(0).getDescription());
            weather.setIcon(weatherDataByCityId.getWeather().get(0).getIcon());
            weather.setDate(weatherDataByCityId.getDt());
            weather.setSunrise(weatherDataByCityId.getSys().getSunrise());
            weather.setSunset(weatherDataByCityId.getSys().getSunset());
            city.setWeather(weather);
        }
        return city;
    }

    public static List<City> mapCities(WeatherDataByCityIds weatherDataByCityIds) {
        ArrayList<City> cities = new ArrayList<>();
        if (weatherDataByCityIds != null) {
            List<WeatherDataByCityId> weatherDataByCityIdList = weatherDataByCityIds.getList();
            for (WeatherDataByCityId weatherDataByCityId : weatherDataByCityIdList) {
                City city = mapCity(weatherDataByCityId);
                cities.add(city);
            }
        }
        return cities;
    }
}
