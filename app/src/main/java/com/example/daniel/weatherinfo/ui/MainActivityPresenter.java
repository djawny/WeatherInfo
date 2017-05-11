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

                    }
                }));
    }

    public void loadCitiesFromNetwork() {
        mCityRepository
                .getCitiesRx()
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler)
                .flatMap(new Function<List<City>, ObservableSource<ResponseByIds>>() {
                    @Override
                    public ObservableSource<ResponseByIds> apply(List<City> cities) throws Exception {
                        String cityIds = getIdsFromCities(cities);
                        return mOpenWeatherMapService.getWeatherByIds(cityIds);
                    }
                })
                .subscribeWith(new DisposableObserver<ResponseByIds>() {
                    @Override
                    public void onNext(ResponseByIds responseByIds) {
                        List<City> cities = Mapper.mapCities(responseByIds);
                        if (!cities.isEmpty()) {
                            saveCitiesToDatabase(cities);
                            getView().displayCities(cities);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private String getIdsFromCities(List<City> cities) {
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

    private void saveCitiesToDatabase(List<City> cities) {
        addDisposable(mCityRepository.saveCitiesRx(cities)
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler)
                .subscribeWith(new DisposableObserver<Void>() {
                    @Override
                    public void onNext(Void value) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }
}
