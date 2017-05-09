package com.example.daniel.weatherinfo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.ui.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class CityPagerAdapter extends FragmentPagerAdapter {

    private List<City> mCities = new ArrayList<>();

    public CityPagerAdapter(FragmentManager fm, List<City> cities) {
        super(fm);
            mCities.clear();
            mCities.addAll(cities);
    }

    public List<City> getCities() {
        return mCities;
    }

    @Override
    public Fragment getItem(int position) {
        City city = mCities.get(position);
        PageFragment fragment = PageFragment.newInstance(city.getId());
        return fragment;
    }

    @Override
    public int getCount() {
        return mCities.size();
    }
}
