package com.daniel.jawny.weatherinfo.data.chart;

import com.daniel.jawny.weatherinfo.data.database.model.Forecast;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseChart {
    private float mMaxTemp;
    private float mMinTemp;
    private List<String> mXAxisLabels;

    public BaseChart() {
        mMaxTemp = Float.MIN_VALUE;
        mMinTemp = Float.MAX_VALUE;
        mXAxisLabels = new ArrayList<>();
    }

    public abstract BaseChart setData(List<Forecast> forecasts);

    protected void setTempExtremeValues(float temp) {
        if (temp > mMaxTemp) {
            mMaxTemp = temp;
        } else if (temp < mMinTemp) {
            mMinTemp = temp;
        }
    }

    public float getMaxTemp() {
        return mMaxTemp;
    }

    public float getMinTemp() {
        return mMinTemp;
    }

    public List<String> getXAxisLabels() {
        return mXAxisLabels;
    }
}
