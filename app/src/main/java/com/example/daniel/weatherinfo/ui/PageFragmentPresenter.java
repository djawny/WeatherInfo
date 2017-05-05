package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BasePresenter;

import io.reactivex.Scheduler;

public class PageFragmentPresenter extends BasePresenter<PageFragmentView> {

    public PageFragmentPresenter(Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
    }


}
