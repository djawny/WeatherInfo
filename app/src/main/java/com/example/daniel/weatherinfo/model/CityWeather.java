package com.example.daniel.weatherinfo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = CityWeather.TABLE_NAME)
public class CityWeather implements Serializable{

    public static final String TABLE_NAME = "cityweather";

    @DatabaseField(columnName = "id")
    private String mId;

    @DatabaseField(columnName = "cityName", canBeNull = false)
    private String mCityName;

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

    public CityWeather() {
    }

    public CityWeather(String id, String cityName, double temp, double tempMin, double tempMax, int humidity, int pressure, String description, String icon, int date) {
        mId = id;
        mCityName = cityName;
        mTemp = temp;
        mTempMin = tempMin;
        mTempMax = tempMax;
        mHumidity = humidity;
        mPressure = pressure;
        mDescription = description;
        mIcon = icon;
        mDate = date;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public double getmTemp() {
        return mTemp;
    }

    public void setmTemp(double mTemp) {
        this.mTemp = mTemp;
    }

    public double getmTempMin() {
        return mTempMin;
    }

    public void setmTempMin(double mTempMin) {
        this.mTempMin = mTempMin;
    }

    public double getmTempMax() {
        return mTempMax;
    }

    public void setmTempMax(double mTempMax) {
        this.mTempMax = mTempMax;
    }

    public int getmHumidity() {
        return mHumidity;
    }

    public void setmHumidity(int mHumidity) {
        this.mHumidity = mHumidity;
    }

    public int getmPressure() {
        return mPressure;
    }

    public void setmPressure(int mPressure) {
        this.mPressure = mPressure;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public int getmDate() {
        return mDate;
    }

    public void setmDate(int mDate) {
        this.mDate = mDate;
    }


}
