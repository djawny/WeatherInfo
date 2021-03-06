package com.daniel.jawny.weatherinfo.data.database.model;

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
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String COUNTRY = "country";
    public static final String WEATHER_ID = "weatherId";
    public static final String FORECASTS = "forecasts";

    @DatabaseField(columnName = ID, id = true)
    private int mId;

    @DatabaseField(columnName = NAME, canBeNull = false)
    private String mName;

    @DatabaseField(columnName = LONGITUDE, canBeNull = false)
    private double mLongitude;

    @DatabaseField(columnName = LATITUDE, canBeNull = false)
    private double mLatitude;

    @DatabaseField(columnName = COUNTRY, canBeNull = false)
    private String mCountry;

    @DatabaseField(columnName = WEATHER_ID, foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Weather mWeather;

    @ForeignCollectionField(columnName = FORECASTS, eager = true)
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

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        mWeather = weather;
    }
}
