package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.adapter.CityAdapter;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;
import com.example.daniel.weatherinfo.util.KeyboardUtils;
import com.example.daniel.weatherinfo.util.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCityActivity extends BaseActivity implements AddCityActivityView, CityAdapter.OnCityCrossClickedListener {

    @BindView(R.id.main_background)
    ImageView mImageView;

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.status_info)
    TextView mStatusInfo;

    @BindView(R.id.city_edit_text)
    EditText mEditText;

    @Inject
    AddCityActivityPresenter mPresenter;

    private CityAdapter mCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        setSupportActionBar(mToolbar);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        initializePresenter();
        setRecycleView();
        loadCities();
    }

    private void initializePresenter() {
        mPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    private void loadCities() {
        mPresenter.loadCitiesFromDatabase();
    }

    private void setRecycleView() {
        mRecycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void displayCities(List<City> cities) {
        mRecycleView.setVisibility(View.VISIBLE);
        mStatusInfo.setVisibility(View.GONE);
        if (mCityAdapter == null) {
            mCityAdapter = new CityAdapter(this, cities, this);
            mRecycleView.setAdapter(mCityAdapter);
        } else {
            mCityAdapter.swapData(cities);
        }
    }

    @Override
    public void showNoData() {
        mRecycleView.setVisibility(View.GONE);
        mStatusInfo.setVisibility(View.VISIBLE);
        mStatusInfo.setText(R.string.message_no_data);
    }

    @Override
    public void showErrorInfo() {
        mRecycleView.setVisibility(View.GONE);
        mStatusInfo.setVisibility(View.VISIBLE);
        mStatusInfo.setText(R.string.message_error);
    }

    @Override
    public void onDelete(int cityId) {
        mPresenter.deleteCityFromDatabase(cityId);
    }

    @Override
    public void onDeleteComplete() {
        loadCities();
    }

    @OnClick(R.id.add_button)
    public void onAddButtonClicked() {
        String cityName = mEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(cityName)) {
            if (NetworkUtils.isNetAvailable(this)) {
                mPresenter.addCityFromNetwork(cityName);
                mEditText.setText("");
                KeyboardUtils.hideSoftKeyboard(this);
            } else {
                KeyboardUtils.hideSoftKeyboard(this);
                showSnackBar("Network error! Check the network connection settings.", Snackbar.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onAddComplete() {
        loadCities();
    }
}
