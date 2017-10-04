package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.ui.base.BasePresenter;
import com.example.daniel.weatherinfo.ui.view.CityListActivityView;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

public class CityListActivityPresenter extends BasePresenter<CityListActivityView> {

    public CityListActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void loadCitiesFromDatabase() {
        addDisposable(getDataManager()
                .getCitiesFromDatabase()
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
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
                        getView().showLoadErrorInfo();
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                }));
    }

    public void deleteCityFromDatabase(int cityId) {
        addDisposable(getDataManager()
                .removeCityFromDatabase(cityId)
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getView().reloadData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showDeleteErrorInfo();
                    }
                }));
    }

    public void addCityFromNetwork(final String apiKey, double lat, double lon, final String language) {
        getView().showAddLocationProgressBar();
        addDisposable(getDataManager()
                .getCityFromNetwork(apiKey, lat, lon, language)
                .subscribeOn(getSubscribeScheduler())
                .flatMap(new Function<City, ObservableSource<City>>() {
                    @Override
                    public ObservableSource<City> apply(City city) throws Exception {
                        return Observable.zip(getDataManager().getForecastsFromNetwork(apiKey, city.getId(), language), Observable.just(city), new BiFunction<List<Forecast>, City, City>() {
                            @Override
                            public City apply(List<Forecast> forecasts, City city) throws Exception {
                                city.setForecastCollection(forecasts);
                                return city;
                            }
                        });
                    }
                }).flatMapCompletable(new Function<City, Completable>() {
                    @Override
                    public Completable apply(City city) throws Exception {
                        return getDataManager().saveCityToDatabase(city);
                    }
                })
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getView().hideAddLocationProgressBar();
                        getView().reloadData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideAddLocationProgressBar();
                        getView().showNetworkErrorInfo();
                    }
                }));
    }

    public void loadCityFromNetwork(final String apiKey, double lat, double lon, final String language) {
        getView().showActualLocationProgressBar();
        addDisposable(getDataManager()
                .getCityFromNetwork(apiKey, lat, lon, language)
                .subscribeOn(getSubscribeScheduler())
                .flatMap(new Function<City, ObservableSource<City>>() {
                    @Override
                    public ObservableSource<City> apply(City city) throws Exception {
                        return Observable.zip(getDataManager().getForecastsFromNetwork(apiKey, city.getId(), language), Observable.just(city), new BiFunction<List<Forecast>, City, City>() {
                            @Override
                            public City apply(List<Forecast> forecasts, City city) throws Exception {
                                city.setForecastCollection(forecasts);
                                return city;
                            }
                        });
                    }
                })
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        getView().hideActualLocationProgressBar();
                        getView().updateActualLocationText(city);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideActualLocationProgressBar();
                        getView().showNetworkErrorInfo();
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                })
        );
    }

    public void saveCityToDatabase(City city) {
        getView().showAddLocationProgressBar();
        addDisposable(getDataManager().saveCityToDatabase(city)
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getView().hideAddLocationProgressBar();
                        getView().reloadData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideAddLocationProgressBar();
                        getView().showSaveErrorInfo();
                    }
                }));
    }
}
