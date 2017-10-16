package com.example.daniel.weatherinfo.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.database.model.Weather;
import com.example.daniel.weatherinfo.di.ApplicationContext;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.example.daniel.weatherinfo.data.database.model.City.NAME;
import static com.example.daniel.weatherinfo.data.database.model.Forecast.CITY_ID;
import static com.example.daniel.weatherinfo.util.AppConstants.DATABASE_NAME;
import static com.example.daniel.weatherinfo.util.AppConstants.DATABASE_VERSION;

@Singleton
public class DatabaseImpl extends OrmLiteSqliteOpenHelper implements Database {

    private RuntimeExceptionDao<City, Integer> mCityDao;
    private RuntimeExceptionDao<Weather, Integer> mWeatherDao;
    private final RuntimeExceptionDao<Forecast, Integer> mForecastDao;

    @Inject
    public DatabaseImpl(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mCityDao = getRuntimeExceptionDao(City.class);
        mWeatherDao = getRuntimeExceptionDao(Weather.class);
        mForecastDao = getRuntimeExceptionDao(Forecast.class);
    }

    @Override
    public void onCreate(final SQLiteDatabase database, final ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, City.class);
            TableUtils.createTableIfNotExists(connectionSource, Weather.class);
            TableUtils.createTableIfNotExists(connectionSource, Forecast.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, City.class, true);
            TableUtils.dropTable(connectionSource, Weather.class, true);
            TableUtils.dropTable(connectionSource, Forecast.class, true);
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
    public City getCity(String cityName) {
        QueryBuilder<City, Integer> queryBuilder = mCityDao.queryBuilder();
        Where<City, Integer> where = queryBuilder.where();
        try {
            return where.eq(NAME, cityName).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveCity(City city) {
        try {
            getWritableDatabase().beginTransaction();
            removeCity(city.getId());
            Weather weather = city.getWeather();
            setWeatherIdIfCityExists(city, weather);
            mWeatherDao.createOrUpdate(weather);
            mCityDao.createOrUpdate(city);
            List<Forecast> forecasts = city.getForecasts();
            for (Forecast forecast : forecasts) {
                forecast.setCity(city);
                mForecastDao.createOrUpdate(forecast);
            }
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
    }

    private void setWeatherIdIfCityExists(City city, Weather weather) {
        City queryCity = mCityDao.queryForId(city.getId());
        if (queryCity != null) {
            weather.setId(queryCity.getWeather().getId());
        }
    }

    @Override
    public void removeCity(int cityId) {
        City city = mCityDao.queryForId(cityId);
        if (city != null) {
            mCityDao.deleteById(cityId);
            mWeatherDao.deleteById(city.getWeather().getId());
            DeleteBuilder<Forecast, Integer> deleteBuilder = mForecastDao.deleteBuilder();
            Where<Forecast, Integer> where = deleteBuilder.where();
            try {
                where.eq(CITY_ID, cityId);
                deleteBuilder.delete();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
