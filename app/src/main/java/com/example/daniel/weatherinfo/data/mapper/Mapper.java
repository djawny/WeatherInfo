package com.example.daniel.weatherinfo.data.mapper;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.database.model.Weather;
import com.example.daniel.weatherinfo.data.network.model.CitiesWeatherData;
import com.example.daniel.weatherinfo.data.network.model.CityForecast;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static City mapCity(CityWeatherData cityWeatherData) {
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

    public static List<City> mapCities(CitiesWeatherData citiesWeatherData) {
        List<City> cities = new ArrayList<>();
        if (citiesWeatherData != null) {
            List<CityWeatherData> dataList = citiesWeatherData.getList();
            for (CityWeatherData cityWeatherData : dataList) {
                City city = mapCity(cityWeatherData);
                cities.add(city);
            }
        }
        return cities;
    }

    public static List<Forecast> mapForecast(CityForecastData cityForecastData, City city) {
        List<Forecast> forecasts = new ArrayList<>();
        if (cityForecastData != null) {
            List<CityForecast> cityForecasts = cityForecastData.getList();
            for (CityForecast cityForecast : cityForecasts) {
                Forecast forecast = new Forecast();
                forecast.setCity(city);
                forecast.setDate(cityForecast.getDate());
                forecast.setDateTxt(cityForecast.getDateTxt());
                forecast.setTemp(cityForecast.getMain().getTemp());
                forecast.setIcon(cityForecast.getWeather().get(0).getIcon());
                forecasts.add(forecast);
            }
        }
        return forecasts;
    }
}
