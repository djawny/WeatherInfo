package com.daniel.jawny.weatherinfo.data.chart;

import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;

import java.util.ArrayList;
import java.util.List;

public abstract class ForecastChart<T extends BarLineScatterCandleBubbleData<?>> {
    private float mMaxTemp;
    private float mMinTemp;
    protected T mData;
    private List<String> mXAxisLabels;

    public ForecastChart() {
        mMaxTemp = Float.MIN_VALUE;
        mMinTemp = Float.MAX_VALUE;
        mXAxisLabels = new ArrayList<>();
    }

    public abstract void setData(List<Forecast> forecasts);

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

    public T getData() {
        return mData;
    }
}
