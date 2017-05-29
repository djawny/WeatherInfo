package com.example.daniel.weatherinfo.ui.adapter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class MyBarChartXAxisValueFormatter implements IAxisValueFormatter {

    public List<String> mValues;

    public MyBarChartXAxisValueFormatter(List<String> mValues) {
        this.mValues = mValues;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues.get((int) value);
    }
}
