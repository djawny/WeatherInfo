package com.example.daniel.weatherinfo.ui.main;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BasePresenter;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void loadCitiesFromDatabase(final int mCurrentCityId) {
        addDisposable(getDataManager()
                .getCitiesFromDatabase()
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<List<City>>() {
                    @Override
                    public void onNext(List<City> cities) {
                        if (!cities.isEmpty()) {
                            getView().setSpinnerList(cities);
                            City city = getCityById(cities, mCurrentCityId);
                            if (city != null) {
                                getView().displayCityData(city);
                            } else {
                                getView().displayCityData(cities.get(0));
                            }
                        } else {
                            getView().showNoData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showDatabaseErrorInfo();
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                })
        );
    }

    private City getCityById(List<City> cities, int mCurrentCityId) {
        for (City city : cities) {
            if (city.getId() == mCurrentCityId) {
                return city;
            }
        }
        return null;
    }

    public void loadCityFromDatabaseByCityId(int cityId) {
        addDisposable(getDataManager()
                .getCityFromDatabase(cityId)
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        getView().displayCityData(city);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showDatabaseErrorInfo();
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                })
        );
    }

    public void loadCityFromDatabaseByCityName(String cityName) {
        addDisposable(getDataManager()
                .getCityFromDatabase(cityName)
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        getView().displayCityData(city);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showDatabaseErrorInfo();
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                })
        );
    }

    public void refreshCityFromNetwork(final String apiKey, final int cityId, final String language) {
        addDisposable(getDataManager()
                .getCityFromNetwork(apiKey, cityId, language)
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
                        getView().reloadData(cityId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showNetworkErrorInfo();
                    }
                }));
    }

    public void loadCurrentCityId() {
        addDisposable(getDataManager()
                .getCurrentCityId()
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer cityId) {
                        getView().setCurrentCityId(cityId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("Current city id load complete");
                    }
                })
        );
    }

    public void saveCurrentCity(int cityId) {
        addDisposable(getDataManager().saveCurrentCityId(cityId)
                .subscribeOn(getSubscribeScheduler())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Timber.i("Current city id save complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, e.getMessage());
                    }
                }));
    }
}
