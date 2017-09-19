package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;
import com.example.daniel.weatherinfo.ui.base.BasePresenter;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;

public class AddCityActivityPresenter extends BasePresenter<AddCityActivityView> {

    public AddCityActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider.io(), schedulerProvider.ui());
    }

    public void addCityFromNetwork(final String apiKey, String cityName) {
        addDisposable(getDataManager()
                .getCityWeatherDataByName(apiKey, cityName)
                .subscribeOn(getSubscribeScheduler())
                .map(new Function<CityWeatherData, City>() {
                    @Override
                    public City apply(CityWeatherData cityWeatherData) throws Exception {
                        return Mapper.mapCity(cityWeatherData);
                    }
                })
                .flatMap(new Function<City, ObservableSource<City>>() {
                    @Override
                    public ObservableSource<City> apply(City city) throws Exception {
                        return Observable.zip(getDataManager().getCityForecastDataById(apiKey, city.getId()), Observable.just(city), new BiFunction<CityForecastData, City, City>() {
                            @Override
                            public City apply(CityForecastData cityForecastData, City city) throws Exception {
                                List<Forecast> forecasts = Mapper.mapForecast(cityForecastData, city);
                                city.setForecastCollection(forecasts);
                                return city;
                            }
                        });
                    }
                }).flatMapCompletable(new Function<City, Completable>() {
                    @Override
                    public Completable apply(City city) throws Exception {
                        return getDataManager().saveCity(city);
                    }
                })
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getView().onAddComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showErrorInfo();
                    }
                }));
    }
}

