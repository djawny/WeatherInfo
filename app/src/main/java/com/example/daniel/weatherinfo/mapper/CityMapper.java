package com.example.daniel.weatherinfo.mapper;

import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.model.OWMResponse;
import com.example.daniel.weatherinfo.model.Weather;

public class CityMapper {

    public CityMapper() {
    }

    public static City mapCity(OWMResponse owmResponse) {
        City city = new City();
        if (owmResponse != null) {
            city.setId(owmResponse.getId());
            city.setName(owmResponse.getName());
            Weather weather = new Weather();
            weather.setId(owmResponse.getId());
            weather.setTemp(owmResponse.getMain().getTemp());
            weather.setTempMin(owmResponse.getMain().getTempMin());
            weather.setTempMax(owmResponse.getMain().getTempMax());
            weather.setHumidity(owmResponse.getMain().getHumidity());
            weather.setPressure(owmResponse.getMain().getPressure());
            weather.setDescription(owmResponse.getWeather().get(0).getDescription());
            weather.setIcon(owmResponse.getWeather().get(0).getIcon());
            weather.setDate(owmResponse.getDt());
            city.setWeather(weather);
        }
        return city;
    }
}
