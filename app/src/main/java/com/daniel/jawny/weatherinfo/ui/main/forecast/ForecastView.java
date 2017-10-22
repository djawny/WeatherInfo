package com.daniel.jawny.weatherinfo.ui.main.forecast;

import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.daniel.jawny.weatherinfo.ui.base.BaseView;

import java.util.List;

public interface ForecastView extends BaseView {

    void drawForecastLineChart(List<Forecast> forecasts);

    void drawForecastBarChart(List<Forecast> forecasts);

    void animateCharts();

    void showDatabaseErrorInfo();
}
