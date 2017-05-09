package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.repository.CityRepositoryInterface;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class AddCityActivityPresenter extends BasePresenter<AddCityActivityView> {

    private CityRepositoryInterface mCityRepository;

    public AddCityActivityPresenter(CityRepository repository, Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mCityRepository = repository;
    }

    public void loadCities() {
        addDisposable(mCityRepository
                .getCitiesRx()
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler)
                .subscribeWith(new DisposableObserver<List<City>>() {
                    @Override
                    public void onNext(List<City> cities) {
                        if (!cities.isEmpty()) {
                            getView().showCities(cities);
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

    public void addCity(String cityName){

    }


    public void deleteCity(City city) {

    }
}
