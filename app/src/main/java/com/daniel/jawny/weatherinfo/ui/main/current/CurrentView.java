package com.daniel.jawny.weatherinfo.ui.main.current;

import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.base.BaseView;

public interface CurrentView extends BaseView {

    void showDatabaseErrorInfo();

    void displayCityData(City city);

    void animateViews();
}
