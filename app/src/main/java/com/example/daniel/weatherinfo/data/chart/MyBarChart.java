package com.example.daniel.weatherinfo.data.chart;

import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.util.DateUtils;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import static com.example.daniel.weatherinfo.util.AppConstants.DATE_DAY_MONTH;

public class MyBarChart extends BaseChart {

    private List<BarEntry> mEntries;

    public MyBarChart() {
        mEntries = new ArrayList<>();
    }

    @Override
    public MyBarChart setData(List<Forecast> forecasts) {
        float barEntryX = 0;
        int dayPeriodIndex = 1;
        final int dayPeriodSize = 8;
        float dayMaxTemp = Float.MIN_VALUE;
        float temp;

        float todayMaxTemp = (float) forecasts.get(0).getCity().getWeather().getTempMax();
        getEntries().add(new BarEntry(barEntryX, todayMaxTemp));
        getXAxisLabels().add("Today");
        setTempExtremeValues(todayMaxTemp);

        int nextDayStartIndex = getNextDayStartIndex(forecasts);
        for (int i = nextDayStartIndex; i < forecasts.size(); i++) {
            temp = (float) forecasts.get(i).getTemp();
            if (dayMaxTemp < temp) {
                dayMaxTemp = temp;
            }
            if (dayPeriodIndex++ == dayPeriodSize) {
                getXAxisLabels().add(DateUtils.getDateFromUTCTimestamp(forecasts.get(i).getDate(), DATE_DAY_MONTH));
                getEntries().add(new BarEntry(++barEntryX, dayMaxTemp));
                dayPeriodIndex = 1;
                dayMaxTemp = Float.MIN_VALUE;
            }
            setTempExtremeValues(temp);
        }
        return this;
    }

    private int getNextDayStartIndex(List<Forecast> forecasts) {
        int startIndex = 0;
        for (int i = 1; i < forecasts.size(); i++) {
            String dateTxt = forecasts.get(i).getDateTxt();
            if (dateTxt.endsWith("00:00:00")) {
                startIndex = i;
                break;
            }
        }
        return startIndex;
    }

    private void setTempExtremeValues(float temp) {
        if (temp > getMaxTemp()) {
            setMaxTemp(temp);
        } else if (temp < getMinTemp()) {
            setMinTemp(temp);
        }
    }

    public List<BarEntry> getEntries() {
        return mEntries;
    }
}
