package com.example.daniel.weatherinfo.util;

public final class AppConstants {

    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final long OK_HTTP_TIMEOUT = 20;  //in seconds

    public final static String DATABASE_NAME = "weather_info";

    public final static int DATABASE_VERSION = 1;
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_NEW_LINE_TIME = "yyyy-MM-dd\nHH:mm:ss";
    public static final String DATE_DAY_MONTH = "dd/MM";
    public static final String TIME = "HH:mm:ss";

    public static final String TIME_HOURS = "HH";

    public static final int CHART_TEMP_OFFSET = 10;
    public static final int LINE_CHART_FORECAST_SIZE = 10;

    public static final float MAP_WORLD_ZOOM = 1;
    public static final float MAP_LANDMASS_ZOOM = 5;
    public static final float MAP_CITY_ZOOM = 10;
    public static final int MAP_ANIMATION_DURATION = 2000;  //milliseconds
    public static final int MAP_ANIMATION_DELAY = 1000;     //milliseconds
}
