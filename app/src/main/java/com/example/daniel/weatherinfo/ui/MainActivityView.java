package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.ui.base.BaseView;
import com.example.daniel.weatherinfo.data.database.model.City;

import java.util.List;

public interface MainActivityView extends BaseView {

    void displayCities(List<City> cities);

    void showNoData();

    void showErrorInfo();
}
