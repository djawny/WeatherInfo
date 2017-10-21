package com.daniel.jawny.weatherinfo.ui.locations;

import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.base.BaseView;

import java.util.List;

public interface LocationsView extends BaseView {

    void setToolbar();

    void setLocation();

    void setPlaceAutoCompleteFragment();

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
