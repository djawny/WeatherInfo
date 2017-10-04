package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.ui.base.BasePresenter;
import com.example.daniel.weatherinfo.ui.view.MainActivityView;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    public static final String CURRENT_CITY_ID = "currentCityId";

    public MainActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, Mapper mapper) {
        super(dataManager, schedulerProvider, mapper);
    }

    public void loadCitiesFromDatabase(final int mCurrentCityId) {
        addDisposable(getDataManager()
                .getCities()
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
                .getCity(cityId)
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
                .getCity(cityName)
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
                .getCityWeatherDataById(apiKey, cityId, language)
                .subscribeOn(getSubscribeScheduler())
                .map(cityWeatherData -> getMapper().mapCity(cityWeatherData))
                .flatMap(new Function<City, ObservableSource<City>>() {
                    @Override
                    public ObservableSource<City> apply(City city) throws Exception {
                        return Observable.zip(getDataManager().getCityForecastDataById(apiKey, city.getId(), language), Observable.just(city), (cityForecastData, city1) -> {
                            List<Forecast> forecasts = getMapper().mapForecast(cityForecastData, city1);
                            city1.setForecastCollection(forecasts);
                            return city1;
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
                        getView().reloadData(cityId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showNetworkErrorInfo();
                    }
                }));
    }

    public void initializeCurrentCityId() {
        addDisposable(getDataManager()
                .getIntSharedPreferences(CURRENT_CITY_ID)
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer cityId) {
                        getView().setCurrentCityId(cityId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //ignore
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                })
        );
    }

    public void saveCurrentCity(int cityId) {
        addDisposable(getDataManager().putIntSharedPreferences(CURRENT_CITY_ID, cityId)
                .subscribeOn(getSubscribeScheduler())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        //ignore
                    }

                    @Override
                    public void onError(Throwable e) {
                        //ignore
                    }
                }));
    }
}
