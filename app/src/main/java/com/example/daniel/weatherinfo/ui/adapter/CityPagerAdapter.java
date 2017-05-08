package com.example.daniel.weatherinfo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.ui.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class CityPagerAdapter extends FragmentPagerAdapter {

//    private List<City> mCities = new ArrayList<>();

    public CityPagerAdapter(FragmentManager fm, List<City> cities) {
        super(fm);
//        mCities.addAll(cities);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PageFragment (defined as a static inner class below).
        return PageFragment.newInstance(position + 1);

//        final City city = mCities.get(position);
//        final PageFragment fragment = PageFragment.newInstance(city.getId());
//        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
//        return mCities.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }
}
