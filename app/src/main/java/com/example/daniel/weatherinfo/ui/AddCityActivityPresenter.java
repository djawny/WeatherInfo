package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.data.network.OpenWeatherMapService;
import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.ResponseByCity;
import com.example.daniel.weatherinfo.data.CityDataManager;
import com.example.daniel.weatherinfo.data.CityDataManagerInterface;
import com.example.daniel.weatherinfo.util.Mapper;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class AddCityActivityPresenter extends BasePresenter<AddCityActivityView> {

    private CityDataManagerInterface mCityDataManager;
    private OpenWeatherMapService mOpenWeatherMapService;

    public AddCityActivityPresenter(CityDataManager cityDataManager, OpenWeatherMapService service,
                                    Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mCityDataManager = cityDataManager;
        mOpenWeatherMapService = service;
    }

    public void loadCitiesFromDatabase() {
        addDisposable(mCityDataManager
                .getCitiesRx()
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
        addDisposable(mOpenWeatherMapService.getWeatherByCity(cityName)
                .subscribeOn(mSubscribeScheduler)
                .flatMap(new Function<ResponseByCity, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(ResponseByCity responseByCity) throws Exception {
                        City city = Mapper.mapCity(responseByCity);
                        return mCityDataManager.saveCityRx(city);
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
        addDisposable(mCityDataManager.removeCityRx(cityId)
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
