package com.daniel.jawny.weatherinfo.data.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class CustomXAxisValueFormatter implements IAxisValueFormatter {

    public List<String> mValues;

    public CustomXAxisValueFormatter(List<String> mValues) {
        this.mValues = mValues;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues.get((int) value);
    }
}
