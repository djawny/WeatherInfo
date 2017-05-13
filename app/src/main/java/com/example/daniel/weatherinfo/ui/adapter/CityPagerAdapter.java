package com.example.daniel.weatherinfo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class CityPagerAdapter extends FragmentStatePagerAdapter {

    private List<City> mCities;

    public CityPagerAdapter(FragmentManager fm, List<City> cities) {
        super(fm);
        mCities = new ArrayList<>();
        if (cities != null) {
            mCities.clear();
            mCities.addAll(cities);
        }
    }

    public List<City> getCities() {
        return mCities;
    }

    @Override
    public Fragment getItem(int position) {
        City city = mCities.get(position);
        return PageFragment.newInstance(city.getId());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mCities.size();
    }

    public void swapData(List<City> cities) {
        if (cities != null) {
            mCities.clear();
            mCities.addAll(cities);
            notifyDataSetChanged();
        }
    }
}
