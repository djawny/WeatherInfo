package com.example.daniel.weatherinfo.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityForecastData {

    @SerializedName("city")
    @Expose
    private City city;

    @SerializedName("list")
    @Expose
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
