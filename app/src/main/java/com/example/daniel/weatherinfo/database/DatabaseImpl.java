package com.example.daniel.weatherinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.model.Weather;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

public class DatabaseImpl extends OrmLiteSqliteOpenHelper implements Database {

    private final static String DATABASE_NAME = "weather_info";
    private final static int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<City, Integer> mCityDao;
    private RuntimeExceptionDao<Weather, Integer> mWeatherDao;

    public DatabaseImpl(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mCityDao = getRuntimeExceptionDao(City.class);
        mWeatherDao = getRuntimeExceptionDao(Weather.class);
    }

    @Override
    public void onCreate(final SQLiteDatabase database, final ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, City.class);
            TableUtils.createTableIfNotExists(connectionSource, Weather.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, City.class, true);
            TableUtils.dropTable(connectionSource, Weather.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    @Override
    public List<City> getCities() {
        return mCityDao.queryForAll();
    }

    @Override
    public City getCity(int cityId) {
        return mCityDao.queryForId(cityId);
    }

    @Override
    public void saveCities(List<City> cities) {
        try {
            getWritableDatabase().beginTransaction();
            for (City city : cities) {
                Weather weather = city.getWeather();
                mWeatherDao.createOrUpdate(weather);
                mCityDao.createOrUpdate(city);
            }
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
    }

    @Override
    public void saveCity(City city) {
        try {
            getWritableDatabase().beginTransaction();
            Weather weather = city.getWeather();
            mWeatherDao.createOrUpdate(weather);
            mCityDao.createOrUpdate(city);
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
    }

    @Override
    public void removeCity(int cityId) {
        City city = mCityDao.queryForId(cityId);
        mCityDao.deleteById(cityId);
        mWeatherDao.deleteById(city.getWeather().getId());
    }

    @Override
    public void removeAllCities() {
        try {
            TableUtils.clearTable(getConnectionSource(), City.class);
            TableUtils.clearTable(getConnectionSource(), Weather.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
