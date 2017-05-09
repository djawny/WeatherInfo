package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.repository.CityRepositoryInterface;

import io.reactivex.Scheduler;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    private CityRepositoryInterface mProductRepository;

    public MainActivityPresenter(CityRepository repository, Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mProductRepository = repository;
    }
}
