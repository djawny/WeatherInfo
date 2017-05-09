package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BaseView;
import com.example.daniel.weatherinfo.model.City;

import java.util.List;

public interface MainActivityView extends BaseView {

    void showCities(List<City> cities);

    void showNoData();

    void showErrorInfo();
}
