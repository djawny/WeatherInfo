package com.daniel.jawny.weatherinfo.ui.main;

import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.base.BaseView;

import java.util.List;

public interface MainView extends BaseView {

    void showSplashDialog();

    void setToolbar();

    void setSwipeRefreshListener();

    void setViewPagerListener();

    void setGyroscopeForPanoramaImageView();

    void displayCityData(City city);

    void showNoData();

    void showDatabaseErrorInfo();

    void showNetworkErrorInfo();

    void showNetworkOfflineInfo();

    void reloadData(int cityId);

    void setSpinnerList(List<City> cities);

    void setCurrentCityId(Integer cityId);

    void hideSwipeRefreshLayoutProgress();

    void dismissSplashDialog();
}
