package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.ui.base.BasePresenter;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.DataManagerInterface;

import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class PageFragmentPresenter extends BasePresenter<PageFragmentView> {

    private DataManagerInterface mDataManager;

    public PageFragmentPresenter(DataManager dataManager, Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mDataManager = dataManager;
    }

    public void loadCityFromDatabase(int cityId) {
        addDisposable(mDataManager
                .getCity(cityId)
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
