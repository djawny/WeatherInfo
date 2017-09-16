package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.util.AppConstants;
import com.example.daniel.weatherinfo.util.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentFragment extends Fragment {

    private static final String ARG_CITY = "city";

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

    @BindView(R.id.card_view_general)
    CardView mCardViewGeneral;

    @BindView(R.id.card_view_details)
    CardView mCardViewDetails;

    public CurrentFragment() {
    }

    public static CurrentFragment newInstance(City city) {
        CurrentFragment fragment = new CurrentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        City city = (City) getArguments().getSerializable(ARG_CITY);
        displayCity(city);
        animateViews();
    }

    private void animateViews() {
        Animation computerAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.magnification);
        mCardViewGeneral.startAnimation(computerAnimation);
        mCardViewDetails.startAnimation(computerAnimation);
    }

    public void displayCity(City city) {
        String icon = city.getWeather().getIcon();
        Picasso.with(getActivity())
                .load("http://openweathermap.org/img/w/" + icon + ".png")
                .into(mIcon);
//        setBackground(icon);
        mCityCountry.setText(String.format("%s, %s", city.getName(), city.getCountry()));
        String date = DateUtils.getDateFromUTCTimestamp(city.getWeather().getDate(), AppConstants.DATE_NEW_LINE_TIME);
        mDate.setText(date);
        mDescription.setText(city.getWeather().getDescription());
        mTemperature.setText(String.format("%s °C", String.valueOf(city.getWeather().getTemp())));
        mWind.setText(String.format("Wind: %s m/s", city.getWeather().getWindSpeed()));
        mCloudiness.setText(String.format("Cloudiness: %s %%", city.getWeather().getCloudiness()));
        mPressure.setText(String.format("Pressure: %s hpa", city.getWeather().getPressure()));
        String sunrise = DateUtils.getDateFromUTCTimestamp(city.getWeather().getSunrise(), AppConstants.TIME);
        mSunrise.setText(String.format("Sunrise: %s", sunrise));
        String sunset = DateUtils.getDateFromUTCTimestamp(city.getWeather().getSunset(), AppConstants.TIME);
        mSunset.setText(String.format("Sunset: %s", sunset));
        List<Forecast> forecasts = city.getForecasts();
    }
}
