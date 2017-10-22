package com.daniel.jawny.weatherinfo.ui.main.current;

import com.daniel.jawny.weatherinfo.data.DataManager;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.base.BasePresenter;
import com.daniel.jawny.weatherinfo.util.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class CurrentPresenter extends BasePresenter<CurrentView> {

    @Inject
    public CurrentPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
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
                        getView().animateViews();
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
}
