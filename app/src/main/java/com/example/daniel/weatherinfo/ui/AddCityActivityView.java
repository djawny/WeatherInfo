package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.ui.base.BaseView;
import com.example.daniel.weatherinfo.data.database.model.City;

import java.util.List;

public interface AddCityActivityView extends BaseView {

    void showErrorInfo();

    void onAddComplete();
}
