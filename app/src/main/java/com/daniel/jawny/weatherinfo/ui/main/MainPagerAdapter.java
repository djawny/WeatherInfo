package com.daniel.jawny.weatherinfo.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.ui.main.current.CurrentFragment;
import com.daniel.jawny.weatherinfo.ui.main.forecast.ForecastFragment;
import com.daniel.jawny.weatherinfo.ui.main.map.MapFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTabTitles;
    private City mCity;

    public MainPagerAdapter(FragmentManager fm, String[] tabTitles, City city) {
        super(fm);
        mTabTitles = tabTitles;
        mCity = city;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CurrentFragment.newInstance(mCity);
            case 1:
                return ForecastFragment.newInstance(mCity);
            case 2:
                return MapFragment.newInstance(mCity);
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

    public void swapData(City city) {
        mCity = city;
        notifyDataSetChanged();
    }
}
