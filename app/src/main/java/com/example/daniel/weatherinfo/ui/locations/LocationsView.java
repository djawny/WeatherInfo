package com.example.daniel.weatherinfo.ui.locations;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BaseView;

import java.util.List;

public interface LocationsView extends BaseView {

    void displayCities(List<City> cities);

    void showNoData();

    void showLoadErrorInfo();

    void showDeleteErrorInfo();

    void showSaveErrorInfo();

    void showNetworkErrorInfo();

    void reloadData();

    void hideAddLocationProgressBar();

    void showAddLocationProgressBar();

    void showActualLocationProgressBar();

    void hideActualLocationProgressBar();

    void updateActualLocationText(City city);
}
