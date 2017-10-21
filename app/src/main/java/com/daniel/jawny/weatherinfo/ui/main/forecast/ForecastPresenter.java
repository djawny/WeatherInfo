package com.daniel.jawny.weatherinfo.ui.main.forecast;

import com.daniel.jawny.weatherinfo.data.DataManager;
import com.daniel.jawny.weatherinfo.data.chart.ForecastBarChart;
import com.daniel.jawny.weatherinfo.data.chart.ForecastChart;
import com.daniel.jawny.weatherinfo.data.chart.ForecastLineChart;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.daniel.jawny.weatherinfo.ui.base.BasePresenter;
import com.daniel.jawny.weatherinfo.util.SchedulerProvider;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class ForecastPresenter extends BasePresenter<ForecastView> {

    @Inject
    public ForecastPresenter(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void loadCityFromDatabaseByCityId(int cityId) {
        addDisposable(getDataManager()
                .getCityFromDatabase(cityId)
                .subscribeOn(getSubscribeScheduler())
                .observeOn(getObserveScheduler())
                .subscribeWith(new DisposableObserver<City>() {
                    @Override
                    public void onNext(City city) {
                        List<Forecast> forecasts = city.getForecasts();
                        ForecastChart<LineData> lineChart = ForecastLineChart.create();
                        lineChart.setData(forecasts);
                        ForecastChart<BarData> barChart = ForecastBarChart.create();
                        barChart.setData(forecasts);
                        getView().drawForecastLineChart(lineChart);
                        getView().drawForecastBarChart(barChart);
                        getView().animateCharts();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showDatabaseErrorInfo();
                    }

                    @Override
                    public void onComplete() {
                        //ignore
                    }
                })
        );
    }
}
