package com.daniel.jawny.weatherinfo.data.chart;

import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.daniel.jawny.weatherinfo.util.TimestampToDateConverter;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import static com.daniel.jawny.weatherinfo.util.AppConstants.LINE_CHART_FORECAST_SIZE;
import static com.daniel.jawny.weatherinfo.util.AppConstants.TIME_HOURS;

public class MyLineChart extends BaseChart {

    private List<Entry> mEntries;

    public MyLineChart() {
        mEntries = new ArrayList<>();
    }

    @Override
    public MyLineChart setData(List<Forecast> forecasts) {
        float temp;
        for (int i = 0; i < LINE_CHART_FORECAST_SIZE; i++) {
            temp = (float) forecasts.get(i).getTemp();
            setTempExtremeValues(temp);
            getEntries().add(new Entry(i, temp));
            getXAxisLabels().add(TimestampToDateConverter.apply(forecasts.get(i).getDate(), TIME_HOURS));
        }
        return this;
    }

    public List<Entry> getEntries() {
        return mEntries;
    }
}
