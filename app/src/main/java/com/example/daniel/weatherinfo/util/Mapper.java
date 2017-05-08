package com.example.daniel.weatherinfo.util;

import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.model.ResponseByCity;
import com.example.daniel.weatherinfo.model.ResponseByIds;
import com.example.daniel.weatherinfo.model.Weather;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public Mapper() {
    }

    public static City mapCity(ResponseByCity response) {
        City city = new City();
        if (response != null) {
            city.setId(response.getId());
            city.setName(response.getName());
            city.setCountry(response.getSys().getCountry());
            Weather weather = new Weather();
            weather.setId(response.getId());
            weather.setTemp(response.getMain().getTemp());
            weather.setTempMin(response.getMain().getTempMin());
            weather.setTempMax(response.getMain().getTempMax());
            weather.setHumidity(response.getMain().getHumidity());
            weather.setCloudiness(response.getClouds().getAll());
            weather.setWindSpeed(response.getWind().getSpeed());
            weather.setWindDegree(response.getWind().getDeg());
            weather.setPressure(response.getMain().getPressure());
            weather.setDescription(response.getWeather().get(0).getDescription());
            weather.setIcon(response.getWeather().get(0).getIcon());
            weather.setDate(response.getDt());
            weather.setSunrise(response.getSys().getSunrise());
            weather.setSunset(response.getSys().getSunset());
            city.setWeather(weather);
        }
        return city;
    }

    public static List<City> mapCities(ResponseByIds response) {
        ArrayList<City> cities = new ArrayList<>();
        if (response != null) {
            List<ResponseByCity> responseByCityList = response.getList();
            for (ResponseByCity responseByCity : responseByCityList) {
                City city = mapCity(responseByCity);
                cities.add(city);
            }
        }
        return cities;
    }
}
