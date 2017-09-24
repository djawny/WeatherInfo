package com.example.daniel.weatherinfo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;
import com.example.daniel.weatherinfo.ui.presenter.AddCityActivityPresenter;
import com.example.daniel.weatherinfo.ui.view.AddCityActivityView;
import com.example.daniel.weatherinfo.util.KeyboardUtils;
import com.example.daniel.weatherinfo.util.NetworkUtils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCityActivity extends BaseActivity implements AddCityActivityView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.spinner)
    ProgressBar mProgressBar;

    @BindView(R.id.city_autocomplete_text_view)
    AutoCompleteTextView mAutoCompleteTextView;

    @Inject
    AddCityActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        mPresenter.setView(this);
        setToolbar();
        setAutoCompleteTextView();
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

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setAutoCompleteTextView() {
        String[] cities = getResources().getStringArray(R.array.city_list);
        List<String> cityList = Arrays.asList(cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cityList);
        mAutoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void showNetworkErrorInfo() {
        mProgressBar.setVisibility(View.INVISIBLE);
        showSnackBar(getString(R.string.message_error_loading_data_from_network), Snackbar.LENGTH_LONG);
    }

    @Override
    public void closeScreen() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        mProgressBar.setVisibility(View.INVISIBLE);
        onBackPressed();
    }

    @OnClick(R.id.add_button)
    public void onAddButtonClicked() {
        String cityName = mAutoCompleteTextView.getText().toString().trim();
        if (!TextUtils.isEmpty(cityName)) {
            if (NetworkUtils.isNetAvailable(this)) {
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.addCityFromNetwork(getString(R.string.open_weather_map_api_key), cityName);
                mAutoCompleteTextView.setText("");
                KeyboardUtils.hideSoftKeyboard(this);
            } else {
                KeyboardUtils.hideSoftKeyboard(this);
                showSnackBar(getString(R.string.message_network_connection_error), Snackbar.LENGTH_LONG);
            }
        }
    }
}
