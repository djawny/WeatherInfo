package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.repository.CityRepositoryInterface;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class PageFragmentPresenter extends BasePresenter<PageFragmentView> {

    private CityRepositoryInterface mCityRepository;

    public PageFragmentPresenter(CityRepository repository, Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mCityRepository = repository;
    }

    public void loadData(int cityId) {
        addDisposable(mCityRepository
                .getCityRx(cityId)
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler)
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        getView().showCity(city);
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
