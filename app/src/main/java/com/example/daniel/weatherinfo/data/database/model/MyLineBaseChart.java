package com.example.daniel.weatherinfo.data.database.model;

import com.example.daniel.weatherinfo.util.DateUtils;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import static com.example.daniel.weatherinfo.util.AppConstants.TIME_HOURES;

public class MyLineBaseChart extends BaseChart {

    private List<Entry> mEntries;

    public MyLineBaseChart() {
        mEntries = new ArrayList<>();
    }

    @Override
    public BaseChart setData(List<Forecast> forecasts) {
        float temp;
        for (int i = 0; i < 10; i++) {
            temp = (float) forecasts.get(i).getTemp();
            if (temp > getYAxisMax()) {
                setYAxisMax(temp);
            } else if (temp < getYAxisMin()) {
                setYAxisMin(temp);
            }
            getEntries().add(new Entry(i, temp));
            getXLabels().add(DateUtils.getDateFromUTCTimestamp(forecasts.get(i).getDate(), TIME_HOURES));
        }
        return this;
    }

    public List<Entry> getEntries() {
        return mEntries;
    }
}
