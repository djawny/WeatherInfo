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
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.repository.CityRepositoryInterface;
import com.example.daniel.weatherinfo.util.DateConverter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PageFragment extends Fragment {

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

    public CityRepositoryInterface mCityRepository = CityRepository.getInstance();
    private Context mContext;

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
        int cityId = getArguments().getInt(ARG_CITY_ID);
        loadData(cityId);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void loadData(int cityId) {
        mCityRepository.getCityRx(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        setViews(city);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setViews(City city) {
        Picasso.with(mContext)
                .load("http://openweathermap.org/img/w/" + city.getWeather().getIcon() + ".png")
                .into(mIcon);
        String date = DateConverter.getDateFromUTCTimestamp(city.getWeather().getDate(), "yyyy-MM-dd HH:mm:ss");
        mDate.setText(date);
        mDescription.setText(city.getWeather().getDescription());
        mTemperature.setText(String.format("%s °C", String.valueOf(city.getWeather().getTemp())));
        mTemperatureRange.setText(String.format("Temperature from %s °C to %s °C", city.getWeather().getTempMin(), city.getWeather().getTempMax()));
        mWind.setText(String.format("Wind %s m/s", city.getWeather().getWindSpeed()));
        mCloudiness.setText(String.format("Cloudiness %s %%", city.getWeather().getCloudiness()));
        mPressure.setText(String.format("Pressure %s hpa", city.getWeather().getPressure()));
        String sunrise = DateConverter.getDateFromUTCTimestamp(city.getWeather().getSunrise(), "HH:mm:ss");
        mSunrise.setText(String.format("Sunrise %s", sunrise));
        String sunset = DateConverter.getDateFromUTCTimestamp(city.getWeather().getSunset(), "HH:mm:ss");
        mSunset.setText(String.format("Sunset %s", sunset));
    }
}
