package com.example.daniel.weatherinfo.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityForecastData {

    @SerializedName("city")
    private City city;

    @SerializedName("list")
    private List<CityForecast> list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<CityForecast> getList() {
        return list;
    }

    public void setList(List<CityForecast> list) {
        this.list = list;
    }
}
