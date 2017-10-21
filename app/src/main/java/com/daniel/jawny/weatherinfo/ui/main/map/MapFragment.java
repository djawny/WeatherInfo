package com.daniel.jawny.weatherinfo.ui.main.map;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniel.jawny.weatherinfo.R;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.di.component.ActivityComponent;
import com.daniel.jawny.weatherinfo.ui.base.BaseFragment;
import com.daniel.jawny.weatherinfo.util.AppConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, MapView {

    private static final String ARG_CITY_ID = "cityId";
    private City mCity;
    private GoogleMap mGoogleMap;

    @Inject
    MapPresenter mPresenter;

    public MapFragment() {
    }

    public static MapFragment newInstance(int cityId) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITY_ID, cityId);
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
        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
            int cityId = getArguments().getInt(ARG_CITY_ID);
            mPresenter.loadCityFromDatabaseByCityId(cityId);
        }
    }

    @Override
    public void displayMap(City city) {
        mCity = city;
        SupportMapFragment mapFragment = getMapFragment();
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private SupportMapFragment getMapFragment() {
        FragmentManager fm;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fm = getFragmentManager();
        } else {
            fm = getChildFragmentManager();
        }
        return (SupportMapFragment) fm.findFragmentById(R.id.map);
    }

    @Override
    public void showDatabaseErrorInfo() {
        //Todo
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        mGoogleMap = googleMap;
        LatLng currentCoordinates = new LatLng(mCity.getLatitude(), mCity.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(currentCoordinates).title(mCity.getName()));
        animateMap();
    }

    public void animateMap() {
        LatLng currentCoordinates = new LatLng(mCity.getLatitude(), mCity.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinates, AppConstants.MAP_LANDMASS_ZOOM));
        new Handler().postDelayed(() -> mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(AppConstants.MAP_CITY_ZOOM)
                , AppConstants.MAP_ANIMATION_DURATION_MILLIS, null), AppConstants.MAP_ANIMATION_DELAY_MILLIS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }
}
