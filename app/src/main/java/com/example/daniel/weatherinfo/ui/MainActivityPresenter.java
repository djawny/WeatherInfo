package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.api.OpenWeatherMapService;
import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.model.ResponseByIds;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.repository.CityRepositoryInterface;
import com.example.daniel.weatherinfo.util.Mapper;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    private CityRepositoryInterface mCityRepository;
    private OpenWeatherMapService mOpenWeatherMapService;

    public MainActivityPresenter(CityRepository repository, OpenWeatherMapService service,
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
                        //ignore
                    }
                }));
    }

    public void loadCitiesFromNetwork() {
        addDisposable(mCityRepository
                .getCitiesRx()
                .subscribeOn(mSubscribeScheduler)
                .flatMap(new Function<List<City>, ObservableSource<ResponseByIds>>() {
                    @Override
                    public ObservableSource<ResponseByIds> apply(List<City> cities) throws Exception {
                        String cityIds = getStringOfCityIdsForApiRequest(cities);
                        return mOpenWeatherMapService.getWeatherByIds(cityIds);
                    }
                })
                .observeOn(mObserveScheduler)
                .flatMap(new Function<ResponseByIds, ObservableSource<Void>>() {
                    @Override
                    public ObservableSource<Void> apply(ResponseByIds responseByIds) throws Exception {
                        List<City> cities = Mapper.mapCities(responseByIds);
                        getView().displayCities(cities);
                        return mCityRepository.saveCitiesRx(cities);
                    }
                })
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
