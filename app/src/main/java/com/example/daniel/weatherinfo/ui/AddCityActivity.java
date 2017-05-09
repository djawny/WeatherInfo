package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.repository.CityRepository;
import com.example.daniel.weatherinfo.ui.adapter.CityAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddCityActivity extends AppCompatActivity implements AddCityActivityView,CityAdapter.OnCityCrossClickedListener {

    @BindView(R.id.main_background)
    ImageView mImageView;

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.status_info)
    TextView mStatusInfo;

    @BindView(R.id.new_city)
    EditText mNewCity;

    @BindView(R.id.add_button)
    ImageButton mAddButton;

    private AddCityActivityPresenter mPresenter;
    private CityAdapter mCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        setSupportActionBar(mToolbar);
        ButterKnife.bind(this);
        mPresenter = new AddCityActivityPresenter(CityRepository.getInstance(), Schedulers.io(), AndroidSchedulers.mainThread());
        mPresenter.setView(this);
        mPresenter.loadCities();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    @Override
    public void showCities(List<City> cities) {
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
    public void onDelete(City city) {
        mPresenter.deleteCity(city);
    }
}
