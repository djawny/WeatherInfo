package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.ui.base.BasePresenter;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityName;
import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.DataManagerInterface;
import com.example.daniel.weatherinfo.data.mapper.Mapper;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class AddCityActivityPresenter extends BasePresenter<AddCityActivityView> {

    private DataManagerInterface mDataManager;

    public AddCityActivityPresenter(DataManager dataManager, Scheduler subscriber, Scheduler observer) {
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

                    }
                }));
    }

    public void addCityFromNetwork(String cityName) {//TODO
        addDisposable(mDataManager.getWeatherDataByCityName(cityName)
                .subscribeOn(mSubscribeScheduler)
                .flatMap(new Function<WeatherDataByCityName, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(WeatherDataByCityName weatherDataByCityName) throws Exception {
                        City city = Mapper.mapCity(weatherDataByCityName);
                        return mDataManager.saveCity(city);
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
                        getView().onAddComplete();
                    }
                }));
    }

    public void deleteCityFromDatabase(int cityId) {
        addDisposable(mDataManager.removeCity(cityId)
                .subscribeOn(mSubscribeScheduler)
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
                        getView().onDeleteComplete();
                    }
                }));
    }
}
