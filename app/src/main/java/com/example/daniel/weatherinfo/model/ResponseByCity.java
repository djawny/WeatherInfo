package com.example.daniel.weatherinfo.model;

import java.util.List;

public class ResponseByCity {

    private ResponseCoordinates coord;
    private List<ResponseWeather> weather;
    private transient String base;
    private ResponseMain main;
    private int visibility;
    private ResponseWind wind;
    private ResponseClouds clouds;
    private int dt;
    private ResponseSys sys;
    private int id;
    private String name;
    private int cod;

    public ResponseCoordinates getCoord() {
        return coord;
    }

    public void setCoord(ResponseCoordinates coord) {
        this.coord = coord;
    }

    public List<ResponseWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<ResponseWeather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public ResponseMain getMain() {
        return main;
    }

    public void setMain(ResponseMain main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public ResponseWind getWind() {
        return wind;
    }

    public void setWind(ResponseWind wind) {
        this.wind = wind;
    }

    public ResponseClouds getClouds() {
        return clouds;
    }

    public void setClouds(ResponseClouds clouds) {
        this.clouds = clouds;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public ResponseSys getSys() {
        return sys;
    }

    public void setSys(ResponseSys sys) {
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
