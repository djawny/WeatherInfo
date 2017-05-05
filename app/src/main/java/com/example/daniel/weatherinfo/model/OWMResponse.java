package com.example.daniel.weatherinfo.model;

import java.util.List;

public class OWMResponse {

    private OWMResponseCoordinates coord;
    private List<OWMResponseWeather> weather;
    private String base;
    private OWMResponseMain main;
    private int visibility;
    private OWMResponseWind wind;
    private OWMResponseClouds clouds;
    private int dt;
    private OWMResponseSys sys;
    private int id;
    private String name;
    private int cod;

    public OWMResponseCoordinates getCoord() {
        return coord;
    }

    public void setCoord(OWMResponseCoordinates coord) {
        this.coord = coord;
    }

    public List<OWMResponseWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<OWMResponseWeather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public OWMResponseMain getMain() {
        return main;
    }

    public void setMain(OWMResponseMain main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public OWMResponseWind getWind() {
        return wind;
    }

    public void setWind(OWMResponseWind wind) {
        this.wind = wind;
    }

    public OWMResponseClouds getClouds() {
        return clouds;
    }

    public void setClouds(OWMResponseClouds clouds) {
        this.clouds = clouds;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public OWMResponseSys getSys() {
        return sys;
    }

    public void setSys(OWMResponseSys sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
