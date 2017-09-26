package com.example.daniel.weatherinfo.ui.view;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BaseView;

import java.util.List;

public interface MainActivityView extends BaseView {

    void displayCityData(City city);

    void showNoData();

    void showDatabaseErrorInfo();

    void showNetworkErrorInfo();

    void reloadData();

    void displayCities(List<City> cities);
}
