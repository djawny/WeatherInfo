package com.example.daniel.weatherinfo.ui.view;

import com.example.daniel.weatherinfo.ui.base.BaseView;
import com.example.daniel.weatherinfo.data.database.model.City;

import java.util.List;

public interface AddCityActivityView extends BaseView {

    void showNetworkErrorInfo();

    void closeScreen();
}
