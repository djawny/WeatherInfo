package com.example.daniel.weatherinfo.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastEntity {

    @SerializedName("dt")
    private long date;

    @SerializedName("main")
    private Main main;

    @SerializedName("clouds")
    private Clouds clouds;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("rain")
    private Rain rain;

    @SerializedName("dt_txt")
    private String dateTxt;

    @SerializedName("weather")
    private List<Weather> weather;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public String getDateTxt() {
        return dateTxt;
    }

    public void setDateTxt(String dateTxt) {
        this.dateTxt = dateTxt;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
}
