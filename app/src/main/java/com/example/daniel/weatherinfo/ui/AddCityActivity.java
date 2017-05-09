package com.example.daniel.weatherinfo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.model.City;
import com.example.daniel.weatherinfo.repository.CityRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddCityActivity extends AppCompatActivity implements AddCityActivityView {

    @BindView(R.id.main_background)
    ImageView mImageView;

    @BindView(R.id.cities_recycle_view)
    RecyclerView mRecycleView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.status_info)
    TextView mStatusInfo;

    private AddCityActivityPresenter mPresenter;

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

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showErrorInfo() {

    }
}
