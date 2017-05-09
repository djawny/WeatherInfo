package com.example.daniel.weatherinfo.ui;

import com.example.daniel.weatherinfo.base.BasePresenter;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.repository.CityRepositoryInterface;

import io.reactivex.Scheduler;

public class PageFragmentPresenter extends BasePresenter<PageFragmentView> {

    private CityRepositoryInterface mCityRepository;

    public PageFragmentPresenter(CityRepository repository, Scheduler subscriber, Scheduler observer) {
        super(subscriber, observer);
        mCityRepository = repository;
    }

}
