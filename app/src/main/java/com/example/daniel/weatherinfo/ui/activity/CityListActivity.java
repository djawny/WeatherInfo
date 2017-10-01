package com.example.daniel.weatherinfo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityListActivity extends BaseActivity implements CityListActivityView, HorizontalCityAdapter.OnRecycleViewItemClickListener {

    public static final String CITY_ID = "city id";
    public static final String CITY_LIST_HAS_BEEN_CHANGED_FLAG = "city list has been changed flag";
    private static final String TAG = "place";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.ready_button)
    TextView mReadyButton;

    @BindView(R.id.add_location_progress_bar)
    ProgressBar mProgressBar;

    @Inject
    CityListActivityPresenter mPresenter;

    private HorizontalCityAdapter mHorizontalCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        mPresenter.setView(this);
        setToolbar();
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

    private void setPlaceAutoCompleteFragment() {
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter onlyCitiesFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        placeAutocompleteFragment.setFilter(onlyCitiesFilter);
        placeAutocompleteFragment.setHint(getString(R.string.search_input_hint));
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                mPresenter.addCityFromNetwork(getString(R.string.open_weather_map_api_key), latLng.latitude, latLng.longitude);
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

    private void setActivityResultCityListHasBeenChanged() {
        Intent intent = getIntent();
        intent.putExtra(CITY_LIST_HAS_BEEN_CHANGED_FLAG, true);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    @Override
    public void displayCities(List<City> cities) {
        mRecyclerView.setVisibility(View.VISIBLE);
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
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadErrorInfo() {
        showSnackBar(getString(R.string.message_error_loading_deleting_data), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showDeleteErrorInfo() {
        showSnackBar(getString(R.string.message_error_loading_deleting_data), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showNetworkErrorInfo() {
        mProgressBar.setVisibility(View.INVISIBLE);
        showSnackBar(getString(R.string.message_error_loading_data_from_network), Snackbar.LENGTH_LONG);
    }

    @Override
    public void reloadData() {
        mPresenter.loadCitiesFromDatabase();
        setActivityResultCityListHasBeenChanged();
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
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
    public void submit(View view) {
        mReadyButton.setVisibility(View.GONE);
        if (mHorizontalCityAdapter != null && mHorizontalCityAdapter.getIsButtonVisibleFlag()) {
            mHorizontalCityAdapter.setIsButtonVisibleFlag(false);
        }
    }
}
