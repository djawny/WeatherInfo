package com.example.daniel.weatherinfo.ui.base;

import com.example.daniel.weatherinfo.data.DataManager;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends BaseView> {

    private V mView;
    private DataManager mDataManager;
    private Scheduler mSubscribeScheduler;
    private Scheduler mObserveScheduler;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BasePresenter(DataManager dataManager, Scheduler subscriber, Scheduler observer) {
        mDataManager = dataManager;
        mSubscribeScheduler = subscriber;
        mObserveScheduler = observer;
    }

    public V getView() {
        return mView;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public Scheduler getSubscribeScheduler() {
        return mSubscribeScheduler;
    }

    public Scheduler getObserveScheduler() {
        return mObserveScheduler;
    }

    public CompositeDisposable getmCompositeDisposable() {
        return mCompositeDisposable;
    }

    public void setView(V view) {
        if (view == null) {
            throw new IllegalArgumentException("Null view in PageFragmentPresenter");
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
