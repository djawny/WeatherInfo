package com.daniel.jawny.weatherinfo.data.chart;

import android.graphics.Color;

import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.daniel.jawny.weatherinfo.util.DateUtils;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import static com.daniel.jawny.weatherinfo.util.AppConstants.DATE_DAY_MONTH;

public class ForecastBarChart extends ForecastChart<BarData> {

    private ForecastBarChart() {
    }

    public static ForecastChart<BarData> create() {
        return new ForecastBarChart();
    }

    @Override
    public void setData(List<Forecast> forecasts) {
        BarDataSet barDataSet = new BarDataSet(getBarEntries(forecasts), "");
        mData = new BarData(barDataSet);
        mData.setValueTextSize(16);
        mData.setHighlightEnabled(false);
        mData.setValueTextColor(Color.WHITE);
        mData.setBarWidth(0.5f);
        mData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> (int) value + "°C");
    }

    private List<BarEntry> getBarEntries(List<Forecast> forecasts) {
        float barEntryX = 0;
        int dayPeriodIndex = 1;
        final int dayPeriodSize = 8;
        float dayMaxTemp = Float.MIN_VALUE;
        float temp;
        List<BarEntry> entries = new ArrayList<>();

        float todayMaxTemp = (float) forecasts.get(0).getCity().getWeather().getTempMax();
        entries.add(new BarEntry(barEntryX, todayMaxTemp));
        getXAxisLabels().add(DateUtils.getTimeStampDate(forecasts.get(0).getDate(), DATE_DAY_MONTH));
        setTempExtremeValues(todayMaxTemp);
        int nextDayStartIndex = getNextDayStartIndex(forecasts);
        for (int i = nextDayStartIndex; i < forecasts.size(); i++) {
            temp = (float) forecasts.get(i).getTemp();
            if (dayMaxTemp < temp) {
                dayMaxTemp = temp;
            }
            if (dayPeriodIndex++ == dayPeriodSize) {
                getXAxisLabels().add(DateUtils.getTimeStampDate(forecasts.get(i).getDate(), DATE_DAY_MONTH));
                entries.add(new BarEntry(++barEntryX, dayMaxTemp));
                dayPeriodIndex = 1;
                dayMaxTemp = Float.MIN_VALUE;
            }
            setTempExtremeValues(temp);
        }
        return entries;
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
}
