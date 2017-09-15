package com.example.daniel.weatherinfo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.adapter.HorizontalCityAdapter;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityListActivity extends BaseActivity implements CityListActivityView, HorizontalCityAdapter.OnRecycleViewItemClickListener {

    private static final int ADD_LOCATION_REQUEST_CODE = 2;
    public static final String CITY_ID = "cityId";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    CityListActivityPresenter mPresenter;

    private HorizontalCityAdapter mHorizontalCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        setToolbar();
        getActivityComponent().inject(this);
        setPresenter();
        setRecycleView();
        loadData();
    }

    private void setPresenter() {
        mPresenter.setView(this);
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadData() {
        mPresenter.loadDataFromDatabase();
    }

    @Override
    public void displayData(List<City> cities) {
        if (mHorizontalCityAdapter == null) {
            mHorizontalCityAdapter = new HorizontalCityAdapter(this, cities, this);
            mRecyclerView.setAdapter(mHorizontalCityAdapter);
        } else {
            mHorizontalCityAdapter.swapData(cities);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_location:
                Intent intent = new Intent(this, AddCityActivity.class);
                startActivityForResult(intent, ADD_LOCATION_REQUEST_CODE);
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showErrorInfo() {

    }

    @Override
    public void onAddComplete() {

    }

    @Override
    public void onDeleteComplete() {
        mPresenter.loadDataFromDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    @Override
    public void deleteClickedItem(int cityId) {
        mPresenter.deleteCityFromDatabase(cityId);
    }

    @Override
    public void showClickedItem(int position) {
        Intent intent = getIntent();
        intent.putExtra(CITY_ID, position);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mHorizontalCityAdapter != null && mHorizontalCityAdapter.getIsButtonVisibleFlag()) {
            mHorizontalCityAdapter.setIsButtonVisibleFlag(false);
        } else {
            super.onBackPressed();
        }
    }
}
