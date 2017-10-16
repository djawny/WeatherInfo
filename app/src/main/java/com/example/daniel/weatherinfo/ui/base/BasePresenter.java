package com.example.daniel.weatherinfo.ui.base;

import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.util.SchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends BaseView> {

    private V mView;
    private DataManager mDataManager;
    private SchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BasePresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        mDataManager = dataManager;
        mSchedulerProvider = schedulerProvider;
    }

    public V getView() {
        return mView;
    }

    protected DataManager getDataManager() {
        return mDataManager;
    }

    protected Scheduler getSubscribeScheduler() {
        return mSchedulerProvider.io();
    }

    protected Scheduler getObserveScheduler() {
        return mSchedulerProvider.ui();
    }

    public void onAttach(V view) {
        if (view == null) {
            throw new IllegalArgumentException("Null view in Presenter");
        }
        mView = view;
    }

    protected void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public void onDetach() {
        mCompositeDisposable.dispose();
        mView = null;
    }
}
