package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.api.OpenWeatherMapService;
import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.model.ResponseByCity;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.repository.CityRepositoryInterface;
import com.example.daniel.weatherinfo.util.Mapper;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class AddCityActivityPresenter extends BasePresenter<AddCityActivityView> {

    private CityRepositoryInterface mCityRepository;
    private OpenWeatherMapService mOpenWeatherMapService;

    public AddCityActivityPresenter(CityRepository repository, OpenWeatherMapService service,
                                    Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mCityRepository = repository;
        mOpenWeatherMapService = service;
    }

    public void loadCitiesFromDatabase() {
        addDisposable(mCityRepository
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
                .flatMap(new Function<ResponseByCity, ObservableSource<Void>>() {
                    @Override
                    public ObservableSource<Void> apply(ResponseByCity responseByCity) throws Exception {
                        City city = Mapper.mapCity(responseByCity);
                        return mCityRepository.saveCityRx(city);
                    }
                })
                .observeOn(mObserveScheduler)
                .subscribeWith(new DisposableObserver<Void>() {
                    @Override
                    public void onNext(Void value) {
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
        addDisposable(mCityRepository.removeCityRx(cityId)
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler)
                .subscribeWith(new DisposableObserver<Void>() {
                    @Override
                    public void onNext(Void value) {
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
