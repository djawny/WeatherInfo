package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;
import com.example.daniel.weatherinfo.util.KeyboardUtils;
import com.example.daniel.weatherinfo.util.NetworkUtils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCityActivity extends BaseActivity implements AddCityActivityView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.city_autocomplete_text_view)
    AutoCompleteTextView mAutoCompleteTextView;

    @Inject
    AddCityActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        setSupportActionBar(mToolbar);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        initializePresenter();
        setAutoCompleteTextView();
    }

    private void setAutoCompleteTextView() {
        String[] cities = getResources().getStringArray(R.array.city_list);
        List<String> cityList = Arrays.asList(cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cityList);
        mAutoCompleteTextView.setAdapter(adapter);
    }

    private void initializePresenter() {
        mPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    @Override
    public void onAddComplete() {

    }

    @Override
    public void showErrorInfo() {
    }

    @OnClick(R.id.add_button)
    public void onAddButtonClicked() {
        String cityName = mAutoCompleteTextView.getText().toString().trim();
        if (!TextUtils.isEmpty(cityName)) {
            if (NetworkUtils.isNetAvailable(this)) {
                mPresenter.addCityFromNetwork(cityName);
                mAutoCompleteTextView.setText("");
                KeyboardUtils.hideSoftKeyboard(this);
            } else {
                KeyboardUtils.hideSoftKeyboard(this);
                showSnackBar("Network error! Check the network connection settings.", Snackbar.LENGTH_LONG);
            }
        }
    }
}
