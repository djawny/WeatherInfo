package com.example.daniel.weatherinfo.ui.view;


import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BaseView;

public interface MainActivityView extends BaseView {

    void displayData(City city);

    void showNoData();

    void showErrorInfo();

    void showNetworkErrorInfo();

    void reloadData();
}
