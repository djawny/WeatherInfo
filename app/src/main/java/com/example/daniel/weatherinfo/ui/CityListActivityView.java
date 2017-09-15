package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BaseView;

import java.util.List;

public interface CityListActivityView extends BaseView {

    void displayData(List<City> cities);

    void showNoData();

    void showErrorInfo();

    void onAddComplete();

    void onDeleteComplete();
}
