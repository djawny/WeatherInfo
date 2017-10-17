package com.daniel.jawny.weatherinfo.ui.main;

import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.base.BaseView;

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
