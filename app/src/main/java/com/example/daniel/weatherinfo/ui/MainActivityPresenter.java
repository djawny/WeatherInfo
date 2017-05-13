package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.DataManagerInterface;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityIds;
import com.example.daniel.weatherinfo.util.Mapper;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    private DataManagerInterface mDataManager;

    public MainActivityPresenter(DataManager dataManager, Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mDataManager = dataManager;
    }

    public void loadCitiesFromDatabase() {
        addDisposable(mDataManager
                .getCities()
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler)
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
        addDisposable(mDataManager
                .getCities()
                .subscribeOn(mSubscribeScheduler)
                .concatMap(new Function<List<City>, ObservableSource<WeatherDataByCityIds>>() {
                    @Override
                    public ObservableSource<WeatherDataByCityIds> apply(List<City> cities) throws Exception {
                        String cityIds = getStringOfCityIdsForApiRequest(cities);
                        return mDataManager.getWeatherDataByCityIds(cityIds);
                    }
                })
                .observeOn(mObserveScheduler)
                .concatMap(new Function<WeatherDataByCityIds, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(WeatherDataByCityIds weatherDataByCityIds) throws Exception {
                        List<City> cities = Mapper.mapCities(weatherDataByCityIds);
                        getView().displayCities(cities);
                        return mDataManager.saveCities(cities).subscribeOn(mSubscribeScheduler);
                    }
                })
                .observeOn(mObserveScheduler)
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
