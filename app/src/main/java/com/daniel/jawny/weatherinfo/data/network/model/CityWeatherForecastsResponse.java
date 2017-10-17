package com.daniel.jawny.weatherinfo.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityWeatherForecastsResponse {

    @SerializedName("city")
    private CityEntity city;

    @SerializedName("list")
    private List<ForecastEntity> list;

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public List<ForecastEntity> getList() {
        return list;
    }

    public void setList(List<ForecastEntity> list) {
        this.list = list;
    }
}
