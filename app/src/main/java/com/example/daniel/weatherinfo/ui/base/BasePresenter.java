package com.example.daniel.weatherinfo.ui.base;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.data.mapper.Mapper;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends BaseView> {

    private V mView;
    private DataManager mDataManager;
    private Mapper mMapper;
    private SchedulerProvider mSchedulerProvider;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BasePresenter(DataManager dataManager, SchedulerProvider schedulerProvider, Mapper mapper) {
        mDataManager = dataManager;
        mSchedulerProvider = schedulerProvider;
        mMapper = mapper;
    }

    public V getView() {
        return mView;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public Mapper getMapper() {
        return mMapper;
    }

    public Scheduler getSubscribeScheduler() {
        return mSchedulerProvider.io();
    }

    public Scheduler getObserveScheduler() {
        return mSchedulerProvider.ui();
    }

    public void setView(V view) {
        if (view == null) {
            throw new IllegalArgumentException("Null view in Presenter");
        }
        mView = view;
    }

    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public void clearDisposable() {
        mCompositeDisposable.clear();
    }
}
