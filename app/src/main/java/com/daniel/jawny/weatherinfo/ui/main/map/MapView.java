package com.daniel.jawny.weatherinfo.ui.main.map;

import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.base.BaseView;

public interface MapView extends BaseView {

    void displayMap(City city);

    void showDatabaseErrorInfo();
}
