package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BaseView;
import com.example.daniel.weatherinfo.model.City;

public interface PageFragmentView extends BaseView {

    void displayCity(City city);
}
