package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.CityDataManager;
import com.example.daniel.weatherinfo.data.CityDataManagerInterface;

import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class PageFragmentPresenter extends BasePresenter<PageFragmentView> {

    private CityDataManagerInterface mCityDataManager;

    public PageFragmentPresenter(CityDataManager cityDataManager, Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mCityDataManager = cityDataManager;
    }

    public void loadCityFromDatabase(int cityId) {
        addDisposable(mCityDataManager
                .getCityRx(cityId)
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler)
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        getView().displayCity(city);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                }));
    }
}
