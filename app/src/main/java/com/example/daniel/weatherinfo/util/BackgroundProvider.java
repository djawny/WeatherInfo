package com.example.daniel.weatherinfo.util;

import com.example.daniel.weatherinfo.R;

public final class BackgroundProvider {

    public static int apply(String cityWeatherIcon) {
        int imageResource;
        switch (cityWeatherIcon) {
            case "01d": //clear sky
                imageResource = R.drawable.day_noclouds;
                break;
            case "02d": //few clouds
            case "03d": //scattered clouds
            case "04d": //broken clouds
            case "50d": //mist
                imageResource = R.drawable.day_clouds;
                break;
            case "09d": //shower rain
            case "10d": //rain
            case "11d": //thunderstorm
                imageResource = R.drawable.day_rain;
                break;
            case "13d": //snow
                imageResource = R.drawable.day_snow;
                break;
            case "01n":
                imageResource = R.drawable.night_noclouds;
                break;
            case "02n":
            case "03n":
            case "04n":
            case "50n":
                imageResource = R.drawable.night_clouds;
                break;
            case "09n":
            case "10n":
            case "11n":
                imageResource = R.drawable.night_rain;
                break;
            case "13n":
                imageResource = R.drawable.night_snow;
                break;
            default:
                imageResource = R.drawable.day_noclouds;
        }
        return imageResource;
    }
}
