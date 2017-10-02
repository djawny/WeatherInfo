package com.example.daniel.weatherinfo.ui.presenter;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.data.network.model.CityForecastData;
import com.example.daniel.weatherinfo.data.network.model.CityWeatherData;
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

    private Mapper mMapper;

    public CityListActivityPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, Mapper mapper) {
        super(dataManager, schedulerProvider.io(), schedulerProvider.ui());
        mMapper = mapper;
    }

    public void loadCitiesFromDatabase() {
        addDisposable(getDataManager()
                .getCities()
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
                .removeCity(cityId)
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
        getView().showAddLocProgressBar();
        addDisposable(getDataManager()
                .getCityWeatherDataByCoordinates(apiKey, lat, lon, language)
                .subscribeOn(getSubscribeScheduler())
                .map(new Function<CityWeatherData, City>() {
                    @Override
                    public City apply(CityWeatherData cityWeatherData) throws Exception {
                        return mMapper.mapCity(cityWeatherData);
                    }
                })
                .flatMap(new Function<City, ObservableSource<City>>() {
                    @Override
                    public ObservableSource<City> apply(City city) throws Exception {
                        return Observable.zip(getDataManager().getCityForecastDataById(apiKey, city.getId(), language), Observable.just(city), new BiFunction<CityForecastData, City, City>() {
                            @Override
                            public City apply(CityForecastData cityForecastData, City city) throws Exception {
                                List<Forecast> forecasts = mMapper.mapForecast(cityForecastData, city);
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
                        getView().hideAddLocProgressBar();
                        getView().reloadData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideAddLocProgressBar();
                        getView().showNetworkErrorInfo();
                    }
                }));
    }
}
