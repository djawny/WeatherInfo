package com.example.daniel.weatherinfo.ui.view;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BaseView;

import java.util.List;

public interface CityListActivityView extends BaseView {

    void displayCities(List<City> cities);

    void showNoData();

    void showLoadErrorInfo();

    void showDeleteErrorInfo();

    void reloadData();
}
