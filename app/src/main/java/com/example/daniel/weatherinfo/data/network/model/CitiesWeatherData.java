package com.example.daniel.weatherinfo.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitiesWeatherData {

    @SerializedName("list")
    @Expose
    private List<CityWeatherData> list;

    public List<CityWeatherData> getList() {
        return list;
    }

    public void setList(List<CityWeatherData> list) {
        this.list = list;
    }
}
