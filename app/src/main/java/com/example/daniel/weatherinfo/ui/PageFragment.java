package com.example.daniel.weatherinfo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.DataManager;
import com.example.daniel.weatherinfo.util.DateConverter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PageFragment extends Fragment implements PageFragmentView {

    @BindView(R.id.city_country)
    TextView mCityCountry;

    @BindView(R.id.icon)
    ImageView mIcon;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.temperature)
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

    private static final String ARG_CITY_ID = "city_id";

    private Context mContext;
    private PageFragmentPresenter mPresenter;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, rootView);
        int cityId = getArguments().getInt(ARG_CITY_ID);
        initializePresenter();
        loadCity(cityId);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    private void initializePresenter() {
        mPresenter = new PageFragmentPresenter(DataManager.getInstance(), Schedulers.io(), AndroidSchedulers.mainThread());
        mPresenter.setView(this);
    }

    private void loadCity(int cityId) {
        mPresenter.loadCityFromDatabase(cityId);
    }

    @Override
    public void displayCity(City city) {
        Picasso.with(mContext)
                .load("http://openweathermap.org/img/w/" + city.getWeather().getIcon() + ".png")
                .into(mIcon);
        mCityCountry.setText(String.format("%s, %s", city.getName(), city.getCountry()));
        String date = DateConverter.getDateFromUTCTimestamp(city.getWeather().getDate(), "yyyy-MM-dd\nHH:mm:ss");
        mDate.setText(date);
        mDescription.setText(city.getWeather().getDescription());
        mTemperature.setText(String.format("%s °C", String.valueOf(city.getWeather().getTemp())));
        mTemperatureRange.setText(String.format("Temperature from %s °C to %s °C", city.getWeather().getTempMin(), city.getWeather().getTempMax()));
        mWind.setText(String.format("Wind: %s m/s", city.getWeather().getWindSpeed()));
        mCloudiness.setText(String.format("Cloudiness: %s %%", city.getWeather().getCloudiness()));
        mPressure.setText(String.format("Pressure: %s hpa", city.getWeather().getPressure()));
        String sunrise = DateConverter.getDateFromUTCTimestamp(city.getWeather().getSunrise(), "HH:mm:ss");
        mSunrise.setText(String.format("Sunrise: %s", sunrise));
        String sunset = DateConverter.getDateFromUTCTimestamp(city.getWeather().getSunset(), "HH:mm:ss");
        mSunset.setText(String.format("Sunset: %s", sunset));
    }
}
