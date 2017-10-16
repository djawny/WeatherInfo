package com.example.daniel.weatherinfo.ui.main;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BaseView;

import java.util.List;

public interface MainView extends BaseView {

    void displayCityData(City city);

    void showNoData();

    void showDatabaseErrorInfo();

    void showNetworkErrorInfo();

    void reloadData(int cityId);

    void setSpinnerList(List<City> cities);

    void setCurrentCityId(Integer cityId);
}
