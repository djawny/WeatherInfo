package com.example.daniel.weatherinfo.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = Weather.TABLE_NAME)
public class Weather implements Serializable {

    public static final String TABLE_NAME = "weather";

    @DatabaseField(columnName = "id", id = true)
    private int mId;

    @DatabaseField(columnName = "temp", canBeNull = false)
    private double mTemp;

    @DatabaseField(columnName = "tempMin", canBeNull = false)
    private double mTempMin;

    @DatabaseField(columnName = "tempMax", canBeNull = false)
    private double mTempMax;

    @DatabaseField(columnName = "humidity", canBeNull = false)
    private int mHumidity;

    @DatabaseField(columnName = "pressure", canBeNull = false)
    private int mPressure;

    @DatabaseField(columnName = "description", canBeNull = false)
    private String mDescription;

    @DatabaseField(columnName = "icon", canBeNull = false)
    private String mIcon;

    @DatabaseField(columnName = "date", canBeNull = false)
    private int mDate;

    @DatabaseField(columnName = "sunrise", canBeNull = false)
    private int mSunrise;

    @DatabaseField(columnName = "sunset", canBeNull = false)
    private int mSunset;

    public Weather() {
    }

    public Weather(int id, double temp, double tempMin, double tempMax, int humidity, int pressure, String description, String icon, int date, int sunrise, int sunset) {
        mId = id;
        mTemp = temp;
        mTempMin = tempMin;
        mTempMax = tempMax;
        mHumidity = humidity;
        mPressure = pressure;
        mDescription = description;
        mIcon = icon;
        mDate = date;
        mSunrise = sunrise;
        mSunset = sunset;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public double getTemp() {
        return mTemp;
    }

    public void setTemp(double temp) {
        mTemp = temp;
    }

    public double getTempMin() {
        return mTempMin;
    }

    public void setTempMin(double tempMin) {
        mTempMin = tempMin;
    }

    public double getTempMax() {
        return mTempMax;
    }

    public void setTempMax(double tempMax) {
        mTempMax = tempMax;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    public int getPressure() {
        return mPressure;
    }

    public void setPressure(int pressure) {
        mPressure = pressure;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getDate() {
        return mDate;
    }

    public void setDate(int date) {
        mDate = date;
    }

    public int getSunrise() {
        return mSunrise;
    }

    public void setSunrise(int sunrise) {
        mSunrise = sunrise;
    }

    public int getSunset() {
        return mSunset;
    }

    public void setSunset(int sunset) {
        mSunset = sunset;
    }
}
