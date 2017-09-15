package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;

import butterknife.ButterKnife;

public class MapFragment extends Fragment {

    private static final String ARG_CITY = "city";

    public MapFragment() {
    }

    public static MapFragment newInstance(City city) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        City city = (City) getArguments().getSerializable(ARG_CITY);
    }
}
