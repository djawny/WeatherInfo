package com.daniel.jawny.weatherinfo.data.chart;

import android.graphics.Color;

import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.daniel.jawny.weatherinfo.util.DateUtils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import static com.daniel.jawny.weatherinfo.util.AppConstants.LINE_CHART_FORECAST_SIZE;
import static com.daniel.jawny.weatherinfo.util.AppConstants.TIME_HOURS;

public class ForecastLineChart extends ForecastChart<LineData> {

    private ForecastLineChart() {
    }

    public static ForecastChart<LineData> create() {
        return new ForecastLineChart();
    }

    @Override
    public void setData(List<Forecast> forecasts) {
        LineDataSet dataSet = new LineDataSet(getEntries(forecasts), "");
        dataSet.setDrawCircles(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(3f);
        dataSet.setFillColor(Color.WHITE);
        dataSet.setDrawValues(false);

        mData = new LineData(dataSet);
        mData.setHighlightEnabled(false);
    }

    private List<Entry> getEntries(List<Forecast> forecasts) {
        float temp;
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < LINE_CHART_FORECAST_SIZE; i++) {
            temp = (float) forecasts.get(i).getTemp();
            setTempExtremeValues(temp);
            entries.add(new Entry(i, temp));
            getXAxisLabels().add(DateUtils.getTimeStampDate(forecasts.get(i).getDate(), TIME_HOURS));
        }
        return entries;
    }
}
