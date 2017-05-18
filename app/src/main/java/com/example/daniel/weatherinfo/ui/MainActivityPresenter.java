package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.data.network.model.CitiesWeatherData;
import com.example.daniel.weatherinfo.ui.base.BasePresenter;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    public MainActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider.io(), schedulerProvider.ui());
    }

    public void loadCitiesFromDatabase() {
        addDisposable(getDataManager()
                .getCities()
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<List<City>>() {
                    @Override
                    public void onNext(List<City> cities) {
                        if (!cities.isEmpty()) {
                            getView().displayCities(cities);
                        } else {
                            getView().showNoData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showErrorInfo();
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                }));
    }

    public void loadCitiesFromNetwork() {
        addDisposable(getDataManager()
                .getCities()
                .subscribeOn(getSubscribeScheduler())
                .concatMap(new Function<List<City>, ObservableSource<CitiesWeatherData>>() {
                    @Override
                    public ObservableSource<CitiesWeatherData> apply(List<City> cities) throws Exception {
                        String cityIds = getStringOfCityIdsForApiRequest(cities);
                        return getDataManager().getCitiesWeatherData(cityIds);
                    }
                })
                .observeOn(getObserveScheduler())
                .concatMap(new Function<CitiesWeatherData, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(CitiesWeatherData citiesWeatherData) throws Exception {
                        List<City> cities = Mapper.mapCities(citiesWeatherData);
                        getView().displayCities(cities);
                        return getDataManager().saveCities(cities).subscribeOn(getSubscribeScheduler());
                    }
                })
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean value) {
                        //ignore
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO
                    }

                    @Override
                    public void onComplete() {
                        //TODO
                    }
                }));
    }

    private String getStringOfCityIdsForApiRequest(List<City> cities) {
        StringBuilder cityIds = new StringBuilder();
        int size = cities.size();
        for (int i = 0; i < size; i++) {
            cityIds.append(String.valueOf(cities.get(i).getId()));
            if (i != (size - 1)) {
                cityIds.append(",");
            }
        }
        return cityIds.toString();
    }
}
