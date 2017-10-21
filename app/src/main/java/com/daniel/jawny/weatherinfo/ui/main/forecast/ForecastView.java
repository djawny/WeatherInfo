package com.daniel.jawny.weatherinfo.ui.main.forecast;

import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.base.BaseView;

public interface ForecastView extends BaseView {

    void drawCharts(City city);

    void showDatabaseErrorInfo();
}
