package com.example.daniel.weatherinfo.data.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DatabaseTable(tableName = City.TABLE_NAME)
public class City implements Serializable {

    public static final String TABLE_NAME = "city";

    @DatabaseField(columnName = "id", id = true)
    private int mId;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;

    @DatabaseField(columnName = "country", canBeNull = false)
    private String mCountry;

    @DatabaseField(columnName = "weatherId", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Weather mWeather;

    @ForeignCollectionField(eager = true)
    private Collection<Forecast> mForecastCollection;

    public List<Forecast> getForecasts() {
        return new ArrayList<>(mForecastCollection);
    }

    public void setForecastCollection(Collection<Forecast> forecastCollection) {
        mForecastCollection = new ArrayList<>(forecastCollection);
    }

    public City() {
    }

    public City(int id, String name, String country, Weather weather) {
        mId = id;
        mName = name;
        mCountry = country;
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

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }
}
