package com.example.daniel.weatherinfo.data.network.model;

import java.util.List;

public class WeatherDataByCityIds {

    private int cnt;
    private List<WeatherDataByCityId> list;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<WeatherDataByCityId> getList() {
        return list;
    }

    public void setList(List<WeatherDataByCityId> list) {
        this.list = list;
    }
}
