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

        return null;
    }

    @Override
    public void saveCities(List<City> cities) {
        try {
            getWritableDatabase().beginTransaction();
            for (City city : cities) {
                Weather weather = city.getWeather();
                mWeatherDao.createIfNotExists(weather);
                mCityDao.create(city);
            }
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
    }

    @Override
    public void saveCity(City city) {

    }

    @Override
    public void removeCity(int cityId) {
        mCityDao.deleteById(cityId);
    }

//    @Override
//    public List<Person> getCityPerson(final String city) {
//        try {
//            QueryBuilder<Person, Integer> queryBuilder = mCityDao.queryBuilder();
//            QueryBuilder<Address, Integer> addressBuilder = mWeatherDao.queryBuilder();
//
//            Where where = addressBuilder.where();
//            where.like("city", "%" + city + "%");
//
//            queryBuilder.join(addressBuilder);
//
//            //queryBuilder query will get all data because we doesn't provide any where
//            //addressBuilder query will get only data where city contains our query city
//
//            return queryBuilder.query();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
//    }
}
