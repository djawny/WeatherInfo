package com.daniel.jawny.weatherinfo.ui.main.pager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniel.jawny.weatherinfo.R;
import com.daniel.jawny.weatherinfo.data.chart.CustomXAxisValueFormatter;
import com.daniel.jawny.weatherinfo.data.chart.ForecastBarChart;
import com.daniel.jawny.weatherinfo.data.chart.ForecastChart;
import com.daniel.jawny.weatherinfo.data.chart.ForecastLineChart;
import com.daniel.jawny.weatherinfo.data.database.model.City;
import com.daniel.jawny.weatherinfo.data.database.model.Forecast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.daniel.jawny.weatherinfo.util.AppConstants.BAR_CHART_ANIMATION_DURATION_MILLIS;
import static com.daniel.jawny.weatherinfo.util.AppConstants.CHARTS_TEMP_OFFSET_DEGREES;
import static com.daniel.jawny.weatherinfo.util.AppConstants.LINE_CHART_ANIMATION_DURATION_MILLIS;

public class ForecastFragment extends Fragment {

    private static final String ARG_CITY = "city";

    @BindView(R.id.line_chart)
    LineChart mLineChart;

    @BindView(R.id.bar_chart)
    BarChart mBarChart;

    public ForecastFragment() {
    }

    public static ForecastFragment newInstance(City city) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        City city = (City) getArguments().getSerializable(ARG_CITY);
        assert city != null;
        List<Forecast> forecasts = city.getForecasts();
        drawForecastLineChart(forecasts);
        drawForecastBarChart(forecasts);
        animateCharts();
    }

    private void drawForecastLineChart(List<Forecast> forecasts) {
        ForecastChart<LineData> chart = ForecastLineChart.create();
        chart.setData(forecasts);

        mLineChart.setDrawGridBackground(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setExtraBottomOffset(20);
        mLineChart.setData(chart.getData());

        YAxis axisLeft = mLineChart.getAxisLeft();
        axisLeft.setAxisMinimum(chart.getMinTemp() - CHARTS_TEMP_OFFSET_DEGREES);
        axisLeft.setAxisMaximum(chart.getMaxTemp() + CHARTS_TEMP_OFFSET_DEGREES);
        axisLeft.setTextSize(16);
        axisLeft.setTextColor(Color.WHITE);
        axisLeft.setDrawGridLines(false);
        axisLeft.setValueFormatter((value, axis) -> (int) value + "°C");

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setLabelCount(9);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new CustomXAxisValueFormatter(chart.getXAxisLabels()));
        xAxis.setTextSize(16);
        xAxis.setTextColor(Color.WHITE);

        mLineChart.invalidate();
    }

    private void drawForecastBarChart(List<Forecast> forecasts) {
        ForecastChart<BarData> chart = ForecastBarChart.create();
        chart.setData(forecasts);

        mBarChart.setDrawGridBackground(false);
        mBarChart.setFitBars(true);
        mBarChart.setScaleEnabled(false);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setExtraBottomOffset(20);
        mBarChart.setData(chart.getData());

        YAxis axisLeft = mBarChart.getAxisLeft();
        if (chart.getMinTemp() - CHARTS_TEMP_OFFSET_DEGREES < 0) {
            axisLeft.setAxisMinimum(chart.getMinTemp() - CHARTS_TEMP_OFFSET_DEGREES);
        } else {
            axisLeft.setAxisMinimum(0);
        }
        axisLeft.setAxisMaximum(chart.getMaxTemp() + CHARTS_TEMP_OFFSET_DEGREES);
        axisLeft.setEnabled(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new CustomXAxisValueFormatter(chart.getXAxisLabels()));
        xAxis.setTextSize(16);
        xAxis.setTextColor(Color.WHITE);

        mBarChart.invalidate();
    }

    public void animateCharts() {
        mLineChart.animateY(LINE_CHART_ANIMATION_DURATION_MILLIS);
        mBarChart.animateX(BAR_CHART_ANIMATION_DURATION_MILLIS);
    }
}
