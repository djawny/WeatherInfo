package com.example.daniel.weatherinfo.util;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {

    public static String getDateFromUTCTimestamp(long timeStamp, String format) {
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

    public static String getDayOfWeek(long timeStamp) {
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timeStamp * 1000L);
        int dayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);
        return dayNames[dayOfWeekNum - 1];
    }
}
