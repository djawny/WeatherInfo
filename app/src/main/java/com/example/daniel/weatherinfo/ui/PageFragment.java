package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BaseFragment;
import com.example.daniel.weatherinfo.util.AppConstants;
import com.example.daniel.weatherinfo.util.DateUtils;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageFragment extends BaseFragment implements PageFragmentView {

    private static final String ARG_CITY_ID = "city_id";

    @BindView(R.id.city_country)
    TextView mCityCountry;

    @BindView(R.id.icon)
    ImageView mIcon;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.temp)
    TextView mTemperature;

    @BindView(R.id.temperature_range)
    TextView mTemperatureRange;

    @BindView(R.id.wind)
    TextView mWind;

    @BindView(R.id.cloudiness)
    TextView mCloudiness;

    @BindView(R.id.pressure)
    TextView mPressure;

    @BindView(R.id.sunrise)
    TextView mSunrise;

    @BindView(R.id.sunset)
    TextView mSunset;

    @Inject
    PageFragmentPresenter mPresenter;

    public PageFragment() {
    }

    public static PageFragment newInstance(int cityId) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITY_ID, cityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivityComponent().inject(this);
        initializePresenter();
        int cityId = getArguments().getInt(ARG_CITY_ID);
        Log.i("Fragment", "cityId=" + cityId);
        loadCity(cityId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    private void initializePresenter() {
        mPresenter.setView(this);
    }

    private void loadCity(int cityId) {
        mPresenter.loadCityFromDatabase(cityId);
    }

    @Override
    public void displayCity(City city) {
        Picasso.with(getActivity())
                .load("http://openweathermap.org/img/w/" + city.getWeather().getIcon() + ".png")
                .into(mIcon);
        mCityCountry.setText(String.format("%s, %s", city.getName(), city.getCountry()));
        String date = DateUtils.getDateFromUTCTimestamp(city.getWeather().getDate(), AppConstants.DATE_NEW_LINE_TIME);
        mDate.setText(date);
        mDescription.setText(city.getWeather().getDescription());
        mTemperature.setText(String.format("%s °C", String.valueOf(city.getWeather().getTemp())));
        mTemperatureRange.setText(String.format("Temperature from %s °C to %s °C", city.getWeather().getTempMin(), city.getWeather().getTempMax()));
        mWind.setText(String.format("Wind: %s m/s", city.getWeather().getWindSpeed()));
        mCloudiness.setText(String.format("Cloudiness: %s %%", city.getWeather().getCloudiness()));
        mPressure.setText(String.format("Pressure: %s hpa", city.getWeather().getPressure()));
        String sunrise = DateUtils.getDateFromUTCTimestamp(city.getWeather().getSunrise(), AppConstants.TIME);
        mSunrise.setText(String.format("Sunrise: %s", sunrise));
        String sunset = DateUtils.getDateFromUTCTimestamp(city.getWeather().getSunset(), AppConstants.TIME);
        mSunset.setText(String.format("Sunset: %s", sunset));
    }
}
