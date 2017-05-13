package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.data.CityDataManager;
import com.example.daniel.weatherinfo.data.CityDataManagerInterface;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.network.OpenWeatherMapService;
import com.example.daniel.weatherinfo.data.network.model.ResponseByIds;
import com.example.daniel.weatherinfo.util.Mapper;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    private CityDataManagerInterface mCityDataManager;
    private OpenWeatherMapService mOpenWeatherMapService;

    public MainActivityPresenter(CityDataManager cityDataManager, OpenWeatherMapService service,
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
                        //ignore
                    }
                }));
    }

    public void loadCitiesFromNetwork() {
        addDisposable(mCityDataManager
                .getCitiesRx()
                .subscribeOn(mSubscribeScheduler)
                .concatMap(new Function<List<City>, ObservableSource<ResponseByIds>>() {
                    @Override
                    public ObservableSource<ResponseByIds> apply(List<City> cities) throws Exception {
                        String cityIds = getStringOfCityIdsForApiRequest(cities);
                        return mOpenWeatherMapService.getWeatherByIds(cityIds);
                    }
                })
                .observeOn(mObserveScheduler)
                .concatMap(new Function<ResponseByIds, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(ResponseByIds responseByIds) throws Exception {
                        List<City> cities = Mapper.mapCities(responseByIds);
                        getView().displayCities(cities);
                        return mCityDataManager.saveCitiesRx(cities).subscribeOn(mSubscribeScheduler);
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
