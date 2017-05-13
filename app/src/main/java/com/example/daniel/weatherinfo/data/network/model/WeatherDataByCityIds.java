package com.example.daniel.weatherinfo.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherDataByCityIds {

    @SerializedName("cnt")
    @Expose
    private int counter;

    @SerializedName("list")
    @Expose
    private List<WeatherDataByCityName> list;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public List<WeatherDataByCityName> getList() {
        return list;
    }

    public void setList(List<WeatherDataByCityName> list) {
        this.list = list;
    }
}
