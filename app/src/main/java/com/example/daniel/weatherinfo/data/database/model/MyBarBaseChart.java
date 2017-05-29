package com.example.daniel.weatherinfo.data.database.model;

import com.example.daniel.weatherinfo.util.DateUtils;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import static com.example.daniel.weatherinfo.util.AppConstants.DATE_DAY_MONTH;

public class MyBarBaseChart extends BaseChart {

    private List<BarEntry> mEntries;

    public MyBarBaseChart() {
        mEntries = new ArrayList<>();
    }

    @Override
    public BaseChart setData(List<Forecast> forecasts) {
        float barEntryX = 0;
        int periodIndex = 1;
        final int periodSize = 8;
        float temp;
        float tempMax = Float.MIN_VALUE;

        float currentDayMaxTemp = (float) forecasts.get(0).getCity().getWeather().getTempMax();
        int startIndex = getStartIndex(forecasts);

        getEntries().add(new BarEntry(barEntryX, currentDayMaxTemp));
        getXLabels().add("Today");
        setYAxisMinMax(currentDayMaxTemp);
        for (int i = startIndex; i < forecasts.size(); i++) {
            temp = (float) forecasts.get(i).getTemp();
            if (tempMax < temp) {
                tempMax = temp;
            }
            if (periodIndex++ == periodSize) {
                getXLabels().add(DateUtils.getDateFromUTCTimestamp(forecasts.get(i).getDate(), DATE_DAY_MONTH));
                getEntries().add(new BarEntry(++barEntryX, tempMax));
                periodIndex = 1;
                tempMax = Float.MIN_VALUE;
            }
            setYAxisMinMax(temp);
        }
        return this;
    }

    private int getStartIndex(List<Forecast> forecasts) {
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

    private void setYAxisMinMax(float temp) {
        if (temp > getYAxisMax()) {
            setYAxisMax(temp);
        } else if (temp < getYAxisMin()) {
            setYAxisMin(temp);
        }
    }

    public List<BarEntry> getEntries() {
        return mEntries;
    }
}
