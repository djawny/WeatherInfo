package com.example.daniel.weatherinfo.data.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = Forecast.TABLE_NAME)
public class Forecast implements Serializable {

    public static final String TABLE_NAME = "forecast";

    @DatabaseField(columnName = "id", generatedId = true)
    private int mId;

    @DatabaseField(columnName = "cityId", foreign = true, foreignAutoRefresh = true)
    private City mCity;

    @DatabaseField(columnName = "date", canBeNull = false)
    private long mDate;

    @DatabaseField(columnName = "dateTxt", canBeNull = false)
    private String mDateTxt;

    @DatabaseField(columnName = "temp", canBeNull = false)
    private double mTemp;

    @DatabaseField(columnName = "icon", canBeNull = false)
    private String mIcon;

    public Forecast() {
    }

    public Forecast(int id, City city, long date, String dateTxt, double temp, String icon) {
        mId = id;
        mCity = city;
        mDate = date;
        mDateTxt = dateTxt;
        mTemp = temp;
        mIcon = icon;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public City getCity() {
        return mCity;
    }

    public void setCity(City city) {
        mCity = city;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public String getDateTxt() {
        return mDateTxt;
    }

    public void setDateTxt(String dateTxt) {
        mDateTxt = dateTxt;
    }

    public double getTemp() {
        return mTemp;
    }

    public void setTemp(double temp) {
        mTemp = temp;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }
}
