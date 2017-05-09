package com.example.daniel.weatherinfo.ui;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PageFragment extends Fragment {

    @BindView(R.id.city_name)
    TextView mCityName;

    @BindView(R.id.country)
    TextView mCountry;

    @BindView(R.id.temperature)
    TextView mTemperature;

    @BindView(R.id.description)
    TextView mDescription;

    private static final String ARG_CITY_ID = "city_id";

    public CityRepositoryInterface mCityRepository = CityRepository.getInstance();

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

    private void loadData(int cityId) {
        mCityRepository.getCityRx(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        mCityName.setText(city.getName());
                        mCountry.setText(city.getCountry());
                        mTemperature.setText(String.valueOf(city.getWeather().getTemp()));
                        mDescription.setText(city.getWeather().getDescription());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
