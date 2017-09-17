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

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    public MainActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider.io(), schedulerProvider.ui());
    }

    public void loadDataFromDatabase() {
        addDisposable(getDataManager()
                .getCities()
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<List<City>>() {
                    @Override
                    public void onNext(List<City> cities) {
                        if (!cities.isEmpty()) {
                            getView().displayData(cities.get(0));
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
                })

        );
    }

    public void loadDataByIdFromDatabase(int cityId) {
        addDisposable(getDataManager()
                .getCity(cityId)
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        getView().displayData(city);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showErrorInfo();
                    }

                    @Override
                    public void onComplete() {

                    }
                })

        );
    }

    public void loadDataByIdFromNetwork(final String apiKey, int cityId) {
        addDisposable(getDataManager()
                .getCityWeatherDataById(apiKey, cityId)
                .subscribeOn(getSubscribeScheduler())
                .concatMap(new Function<CityWeatherData, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(CityWeatherData cityWeatherData) throws Exception {
                        City city = Mapper.mapCity(cityWeatherData);
                        return Observable.zip(getDataManager().getCityForecastDataById(apiKey, city.getId()), Observable.just(city), new BiFunction<CityForecastData, City, Boolean>() {
                            @Override
                            public Boolean apply(CityForecastData cityForecastData, City city) throws Exception {
                                List<Forecast> forecasts = Mapper.mapForecast(cityForecastData, city);
                                city.setForecastCollection(forecasts);
                                return getDataManager()
                                        .saveCity(city)
                                        .subscribeOn(getSubscribeScheduler())
                                        .blockingFirst();
                            }
                        });
                    }
                })
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean value) {
                        //ignore
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showErrorInfo();
                    }

                    @Override
                    public void onComplete() {
                        getView().onRefreshComplete();
                    }
                }));

    }
}
