package com.example.daniel.weatherinfo.util;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {

    public static String getDateFromUTCTimestamp(long mTimestamp, String format) {
        String date = null;
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(mTimestamp * 1000L);
            date = DateFormat.format(format, cal.getTimeInMillis()).toString();
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
