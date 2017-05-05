package com.example.daniel.weatherinfo.base;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends BaseView> {

    private V mView;
    protected Scheduler mSubscribeScheduler;
    protected Scheduler mObserveScheduler;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BasePresenter(Scheduler subscriber, Scheduler observer) {
        mSubscribeScheduler = subscriber;
        mObserveScheduler = observer;
    }

    public V getView() {
        return mView;
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

    public void clearDisposible() {
        mCompositeDisposable.clear();
    }
}
