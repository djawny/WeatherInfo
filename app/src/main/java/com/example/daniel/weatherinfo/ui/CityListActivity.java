package com.example.daniel.weatherinfo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.adapter.HorizontalCityAdapter;
import com.example.daniel.weatherinfo.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityListActivity extends BaseActivity implements CityListActivityView, HorizontalCityAdapter.OnRecycleViewItemClickListener {

    private static final int ADD_CITY_REQUEST_CODE = 2;
    public static final String CITY_ID = "cityId";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.ready_button)
    TextView mReadyButton;

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
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadData() {
        mPresenter.loadAllCitiesFromDatabase();
    }

    @Override
    public void displayData(List<City> cities) {
        mRecyclerView.setVisibility(View.VISIBLE);
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
                startActivityForResult(intent, ADD_CITY_REQUEST_CODE);
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mPresenter.loadAllCitiesFromDatabase();
            }
        }
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
    public void onDeleteComplete() {
        mPresenter.loadAllCitiesFromDatabase();
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
