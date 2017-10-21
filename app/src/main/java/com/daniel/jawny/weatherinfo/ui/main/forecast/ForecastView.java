package com.daniel.jawny.weatherinfo.ui.main.forecast;

import com.daniel.jawny.weatherinfo.data.chart.ForecastChart;
import com.daniel.jawny.weatherinfo.ui.base.BaseView;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;

public interface ForecastView extends BaseView {

    void drawForecastLineChart(ForecastChart<LineData> chart);

    void drawForecastBarChart(ForecastChart<BarData> chart);

    void animateCharts();

    void showDatabaseErrorInfo();
}
