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
    private int mCityId;

    public MainPagerAdapter(FragmentManager fm, String[] tabTitles, int cityId) {
        super(fm);
        mTabTitles = tabTitles;
        mCityId = cityId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CurrentFragment.newInstance(mCityId);
            case 1:
                return ForecastFragment.newInstance(mCityId);
            case 2:
                return MapFragment.newInstance(mCityId);
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

    public void swapData(int cityId) {
        mCityId = cityId;
        notifyDataSetChanged();
    }
}
