package com.example.daniel.weatherinfo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = City.TABLE_NAME)
public class City implements Serializable {

    public static final String TABLE_NAME = "city";

    @DatabaseField(columnName = "id")
    private int mId;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;

    @DatabaseField(columnName = "weatherId", foreign = true, foreignAutoRefresh = true)
    private Weather mWeather;

    public City() {
    }

    public City(int id, String name, Weather weather) {
        mId = id;
        mName = name;
        mWeather = weather;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        mWeather = weather;
    }
}
