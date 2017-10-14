package com.example.daniel.weatherinfo.util;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.TimeZone;

public final class TimestampToDateConverter {

    public static String apply(long timeStamp, String format) {
        String date = null;
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(timeStamp * 1000L);
            date = DateFormat.format(format, cal.getTimeInMillis()).toString();
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
