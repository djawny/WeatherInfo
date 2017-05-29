package com.example.daniel.weatherinfo.data.database.model;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseChart {
    private float mYAxisMax;
    private float mYAxisMin;
    private List<String> mXLabels;

    public BaseChart() {
        mYAxisMax = Float.MIN_VALUE;
        mYAxisMin = Float.MAX_VALUE;
        mXLabels = new ArrayList<>();
    }

    public abstract BaseChart setData(List<Forecast> forecasts);

    public float getYAxisMax() {
        return mYAxisMax;
    }

    public void setYAxisMax(float yAxisMax) {
        mYAxisMax = yAxisMax;
    }

    public float getYAxisMin() {
        return mYAxisMin;
    }

    public void setYAxisMin(float yAxisMin) {
        mYAxisMin = yAxisMin;
    }

    public List<String> getXLabels() {
        return mXLabels;
    }
}
