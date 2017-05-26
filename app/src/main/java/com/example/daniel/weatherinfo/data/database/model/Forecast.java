package com.example.daniel.weatherinfo.data.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = Forecast.TABLE_NAME)
public class Forecast implements Serializable {

    public static final String TABLE_NAME = "forecast";
    public static final String ID = "id";
    public static final String CITY_ID = "cityId";
    public static final String DATE = "date";
    public static final String DATE_TXT = "dateTxt";
    public static final String TEMP = "temp";
    public static final String ICON = "icon";

    @DatabaseField(columnName = ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = CITY_ID, foreign = true, foreignAutoRefresh = true)
    private City mCity;

    @DatabaseField(columnName = DATE, canBeNull = false)
    private long mDate;

    @DatabaseField(columnName = DATE_TXT, canBeNull = false)
    private String mDateTxt;

    @DatabaseField(columnName = TEMP, canBeNull = false)
    private double mTemp;

    @DatabaseField(columnName = ICON, canBeNull = false)
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
