package com.example.daniel.weatherinfo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = City.TABLE_NAME)
public class City implements Serializable {

    public static final String TABLE_NAME = "city";

    @DatabaseField(columnName = "id")
    private String mId;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;

    @DatabaseField(columnName = "cityId", foreign = true, foreignAutoRefresh = true)
    private Weather mAddress;

    public City() {
    }

    public City(String id, String name, Weather address) {
        mId = id;
        mName = name;
        mAddress = address;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Weather getAddress() {
        return mAddress;
    }

    public void setAddress(Weather address) {
        mAddress = address;
    }
}
