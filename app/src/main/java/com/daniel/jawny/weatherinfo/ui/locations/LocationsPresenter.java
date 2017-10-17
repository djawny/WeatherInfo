package com.daniel.jawny.weatherinfo.ui.locations;

import com.daniel.jawny.weatherinfo.data.DataManager;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.di.PerActivity;
import com.daniel.jawny.weatherinfo.ui.base.BasePresenter;
import com.daniel.jawny.weatherinfo.util.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

@PerActivity
public class LocationsPresenter extends BasePresenter<LocationsView> {

    @Inject
    public LocationsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
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
                        return Observable.zip(getDataManager().getForecastsFromNetwork(apiKey, city.getId(), language), Observable.just(city), (forecasts, city1) -> {
                            city1.setForecastCollection(forecasts);
                            return city1;
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
                        return Observable.zip(getDataManager().getForecastsFromNetwork(apiKey, city.getId(), language), Observable.just(city), (forecasts, city1) -> {
                            city1.setForecastCollection(forecasts);
                            return city1;
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
