package com.example.daniel.weatherinfo.ui.main.pager;

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
import com.example.daniel.weatherinfo.util.AppConstants;
import com.example.daniel.weatherinfo.util.TimestampToDateConverter;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentFragment extends Fragment {

    private static final String ARG_CITY = "city";

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

    public void animateViews() {
        Animation computerAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.magnification);
        mCardViewGeneral.startAnimation(computerAnimation);
        mCardViewDetails.startAnimation(computerAnimation);
    }

    public void displayCity(City city) {
        String icon = city.getWeather().getIcon();
        Picasso.with(getActivity())
                .load("http://openweathermap.org/img/w/" + icon + ".png")
                .into(mIcon);
        String date = TimestampToDateConverter.apply(city.getWeather().getDate(), AppConstants.DATE_NEW_LINE_TIME);
        mDate.setText(date);
        mDescription.setText(city.getWeather().getDescription());
        mTemperature.setText(String.format(Locale.getDefault(), "%.1f °C", city.getWeather().getTemp()));
        mWind.setText(String.format(getString(R.string.details_text_wind) + ": %s m/s", city.getWeather().getWindSpeed()));
        mCloudiness.setText(String.format(getString(R.string.details_text_cloudiness) + ": %s %%", city.getWeather().getCloudiness()));
        mPressure.setText(String.format(getString(R.string.details_text_pressure) + ": %s hpa", city.getWeather().getPressure()));
        String sunrise = TimestampToDateConverter.apply(city.getWeather().getSunrise(), AppConstants.TIME);
        mSunrise.setText(String.format(getString(R.string.details_text_sunrise) + ": %s", sunrise));
        String sunset = TimestampToDateConverter.apply(city.getWeather().getSunset(), AppConstants.TIME);
        mSunset.setText(String.format(getString(R.string.details_text_sunset) + ": %s", sunset));
    }
}