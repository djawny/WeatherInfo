package com.example.daniel.weatherinfo.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.adapter.HorizontalCityAdapter;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;
import com.example.daniel.weatherinfo.ui.presenter.CityListActivityPresenter;
import com.example.daniel.weatherinfo.ui.view.CityListActivityView;
import com.example.daniel.weatherinfo.util.AppConstants;
import com.example.daniel.weatherinfo.util.LanguageProvider;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityListActivity extends BaseActivity implements CityListActivityView,
        HorizontalCityAdapter.OnRecycleViewItemClickListener, LocationListener {

    public static final String CITY_ID = "city id";
    public static final String CITY_LIST_HAS_BEEN_CHANGED_FLAG = "city list has been changed flag";
    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view_header)
    TextView mRecyclerViewHeader;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.ready_button)
    TextView mReadyButton;

    @BindView(R.id.add_location_progress_bar)
    ProgressBar mAddLocationProgressBar;

    @BindView(R.id.actual_location_text)
    TextView mActualLocationText;

    @BindView(R.id.actual_location_progress_bar)
    ProgressBar mActualLocationProgressBar;

    @Inject
    CityListActivityPresenter mPresenter;

    private HorizontalCityAdapter mHorizontalCityAdapter;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private boolean mLocationPermissionGranted;
    private City mActualCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        mPresenter.setView(this);
        setToolbar();
        setLocation();
        setPlaceAutoCompleteFragment();
        mPresenter.loadCitiesFromDatabase();
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(AppConstants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(AppConstants.FASTEST_INTERVAL);
        mLocationCallback = new LocationCallback();
    }

    private void setPlaceAutoCompleteFragment() {
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter onlyCitiesFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        placeAutocompleteFragment.setFilter(onlyCitiesFilter);
        placeAutocompleteFragment.setHint(getString(R.string.search_input_hint));
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                mPresenter.addCityFromNetwork(getString(R.string.open_weather_map_api_key), latLng.latitude, latLng.longitude, LanguageProvider.apply());
            }

            @Override
            public void onError(Status status) {
                showNetworkErrorInfo();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    @Override
    public void displayCities(List<City> cities) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerViewHeader.setVisibility(View.VISIBLE);
        if (mHorizontalCityAdapter == null) {
            initializeRecycleView(cities);
        } else {
            mHorizontalCityAdapter.swapData(cities);
        }
    }

    private void initializeRecycleView(List<City> cities) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mHorizontalCityAdapter = new HorizontalCityAdapter(this, cities, this);
        mRecyclerView.setAdapter(mHorizontalCityAdapter);
    }

    @Override
    public void showNoData() {
        mRecyclerViewHeader.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mReadyButton.setVisibility(View.GONE);
    }

    @Override
    public void showLoadErrorInfo() {
        showSnackBar(getString(R.string.message_error_loading_deleting_saving_data), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showDeleteErrorInfo() {
        showSnackBar(getString(R.string.message_error_loading_deleting_saving_data), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showSaveErrorInfo() {
        showSnackBar(getString(R.string.message_error_loading_deleting_saving_data), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showNetworkErrorInfo() {
        showSnackBar(getString(R.string.message_error_loading_data_from_network), Snackbar.LENGTH_LONG);
    }

    @Override
    public void reloadData() {
        mPresenter.loadCitiesFromDatabase();
        setActivityResultCityListHasBeenChanged();
    }

    private void setActivityResultCityListHasBeenChanged() {
        Intent intent = getIntent();
        intent.putExtra(CITY_LIST_HAS_BEEN_CHANGED_FLAG, true);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void hideAddLocationProgressBar() {
        mAddLocationProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAddLocationProgressBar() {
        mAddLocationProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showActualLocationProgressBar() {
        mActualLocationProgressBar.setVisibility(View.VISIBLE);
        mActualLocationText.setText(getString(R.string.text_locating));
    }

    @Override
    public void hideActualLocationProgressBar() {
        mActualLocationProgressBar.setVisibility(View.GONE);
        mActualLocationText.setText(getString(R.string.text_find_actual_location));
    }

    @Override
    public void updateActualLocationText(City city) {
        mActualLocationText.setText(String.format("%s, %s", city.getName(), city.getCountry()));
        mActualCity = city;
    }

    @Override
    public void deleteClickedItem(int cityId) {
        mPresenter.deleteCityFromDatabase(cityId);
    }

    @Override
    public void showClickedItem(int cityId) {
        setActivityResultCityToDisplay(cityId);
        onBackPressed();
    }

    private void setActivityResultCityToDisplay(int cityId) {
        Intent intent = getIntent();
        intent.putExtra(CITY_ID, cityId);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void updateView() {
        mReadyButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ready_button)
    public void onReadyButtonClicked(View view) {
        mReadyButton.setVisibility(View.GONE);
        if (mHorizontalCityAdapter != null && mHorizontalCityAdapter.getIsButtonVisibleFlag()) {
            mHorizontalCityAdapter.setIsButtonVisibleFlag(false);
        }
    }

    @OnClick(R.id.actual_location_text)
    public void onActualLocationTextClicked(View view) {
        if (mActualCity == null) {
            getDeviceLocation();
        } else {
            mPresenter.saveCityToDatabase(mActualCity);
            mActualLocationText.setText(getString(R.string.text_find_actual_location));
            mActualCity = null;
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        mPresenter.loadCityFromNetwork(getString(R.string.open_weather_map_api_key), location.getLatitude(), location.getLongitude(), LanguageProvider.apply());
                    } else {
                        showSnackBar(getString(R.string.message_error_finding_location), Snackbar.LENGTH_LONG);
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                    }
                });
            } else {
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_LOCATION);
            } else {
                mLocationPermissionGranted = true;
                getDeviceLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
}
