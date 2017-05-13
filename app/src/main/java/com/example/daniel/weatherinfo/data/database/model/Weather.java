package com.example.daniel.weatherinfo.data.database.model;


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

    @DatabaseField(columnName = "cloudiness", canBeNull = false)
    private int mCloudiness;

    @DatabaseField(columnName = "windSpeed", canBeNull = false)
    private double mWindSpeed;

    @DatabaseField(columnName = "windDegree", canBeNull = false)
    private double mWindDegree;

    @DatabaseField(columnName = "pressure", canBeNull = false)
    private int mPressure;

    @DatabaseField(columnName = "description", canBeNull = false)
    private String mDescription;

    @DatabaseField(columnName = "icon", canBeNull = false)
    private String mIcon;

    @DatabaseField(columnName = "date", canBeNull = false)
    private long mDate;

    @DatabaseField(columnName = "sunrise", canBeNull = false)
    private long mSunrise;

    @DatabaseField(columnName = "sunset", canBeNull = false)
    private long mSunset;

    public Weather() {
    }

    public Weather(int id, double temp, double tempMin, double tempMax, int humidity, int cloudiness, double windSpeed, double windDegree, int pressure, String description, String icon, long date, long sunrise, long sunset) {
        mId = id;
        mTemp = temp;
        mTempMin = tempMin;
        mTempMax = tempMax;
        mHumidity = humidity;
        mCloudiness = cloudiness;
        mWindSpeed = windSpeed;
        mWindDegree = windDegree;
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

    public int getCloudiness() {
        return mCloudiness;
    }

    public void setCloudiness(int cloudiness) {
        mCloudiness = cloudiness;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }

    public double getWindDegree() {
        return mWindDegree;
    }

    public void setWindDegree(double windDegree) {
        mWindDegree = windDegree;
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

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public long getSunrise() {
        return mSunrise;
    }

    public void setSunrise(long sunrise) {
        mSunrise = sunrise;
    }

    public long getSunset() {
        return mSunset;
    }

    public void setSunset(long sunset) {
        mSunset = sunset;
    }
}
