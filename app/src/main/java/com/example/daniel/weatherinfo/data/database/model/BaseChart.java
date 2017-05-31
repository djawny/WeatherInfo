package com.example.daniel.weatherinfo.data.database.model;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseChart{
    private float mMaxTemp;
    private float mMinTemp;
    private List<String> mXAxisLabels;

    public BaseChart() {
        mMaxTemp = Float.MIN_VALUE;
        mMinTemp = Float.MAX_VALUE;
        mXAxisLabels = new ArrayList<>();
    }

    public abstract BaseChart setData(List<Forecast> forecasts);

    public float getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(float yAxisMax) {
        mMaxTemp = yAxisMax;
    }

    public float getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(float yAxisMin) {
        mMinTemp = yAxisMin;
    }

    public List<String> getXAxisLabels() {
        return mXAxisLabels;
    }
}
