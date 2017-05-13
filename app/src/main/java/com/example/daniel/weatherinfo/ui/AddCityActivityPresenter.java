package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.data.network.OpenWeatherMapService;
import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.model.WeatherDataByCityId;
import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.DataManagerInterface;
import com.example.daniel.weatherinfo.util.Mapper;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class AddCityActivityPresenter extends BasePresenter<AddCityActivityView> {

    private DataManagerInterface mCityDataManager;
    private OpenWeatherMapService mOpenWeatherMapService;

    public AddCityActivityPresenter(DataManager dataManager, OpenWeatherMapService service,
                                    Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mCityDataManager = dataManager;
        mOpenWeatherMapService = service;
    }

    public void loadCitiesFromDatabase() {
        addDisposable(mCityDataManager
                .getCitiesFromDB()
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
        addDisposable(mOpenWeatherMapService.getWeatherDataByCityName(cityName)
                .subscribeOn(mSubscribeScheduler)
                .flatMap(new Function<WeatherDataByCityId, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(WeatherDataByCityId weatherDataByCityId) throws Exception {
                        City city = Mapper.mapCity(weatherDataByCityId);
                        return mCityDataManager.saveCityToDB(city);
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
        addDisposable(mCityDataManager.removeCityFromDB(cityId)
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
