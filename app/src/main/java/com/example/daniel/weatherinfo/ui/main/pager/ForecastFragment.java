package com.example.daniel.weatherinfo.ui.main.pager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.chart.MyBarChart;
import com.example.daniel.weatherinfo.data.chart.MyLineChart;
import com.example.daniel.weatherinfo.data.chart.CustomXAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.daniel.weatherinfo.util.AppConstants.CHART_TEMP_OFFSET;

public class ForecastFragment extends Fragment {

    private static final String ARG_CITY = "city";

    @BindView(R.id.line_chart)
    LineChart mLineChart;

    @BindView(R.id.bar_chart)
    BarChart mBarChart;

    @BindView(R.id.card_view_line_chart)
    CardView mCardViewLC;

    @BindView(R.id.card_view_bar_chart)
    CardView mCardViewBC;

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
        drawLineChart(forecasts);
        drawBarChart(forecasts);
        animateViews();
    }

    public void animateViews() {
        Animation computerAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.magnification);
        mCardViewBC.startAnimation(computerAnimation);
        mCardViewLC.startAnimation(computerAnimation);
    }

    private void drawLineChart(List<Forecast> forecasts) {
        MyLineChart chart = new MyLineChart();
        chart.setData(forecasts);

        LineDataSet dataSet = new LineDataSet(chart.getEntries(), "Label");
        dataSet.setDrawCircles(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(3f);
        dataSet.setFillColor(Color.WHITE);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        lineData.setHighlightEnabled(false);

        mLineChart.setDrawGridBackground(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setExtraBottomOffset(20);
        mLineChart.setData(lineData);

        YAxis axisLeft = mLineChart.getAxisLeft();
        axisLeft.setAxisMinimum(chart.getMinTemp() - CHART_TEMP_OFFSET);
        axisLeft.setAxisMaximum(chart.getMaxTemp() + CHART_TEMP_OFFSET);
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

    private void drawBarChart(List<Forecast> forecasts) {
        MyBarChart chart = new MyBarChart();
        chart.setData(forecasts);

        BarDataSet barDataSet = new BarDataSet(chart.getEntries(), "ForecastEntity");
        BarData barData = new BarData(barDataSet);
        barData.setValueTextSize(16);
        barData.setHighlightEnabled(false);
        barData.setValueTextColor(Color.WHITE);
        barData.setBarWidth(0.5f);
        barData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> (int) value + "°C");

        mBarChart.setDrawGridBackground(false);
        mBarChart.setFitBars(true);
        mBarChart.setScaleEnabled(false);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setExtraBottomOffset(20);
        mBarChart.setData(barData);

        YAxis axisLeft = mBarChart.getAxisLeft();
        if (chart.getMinTemp() - CHART_TEMP_OFFSET < 0) {
            axisLeft.setAxisMinimum(chart.getMinTemp() - CHART_TEMP_OFFSET);
        } else {
            axisLeft.setAxisMinimum(0);
        }
        axisLeft.setAxisMaximum(chart.getMaxTemp() + CHART_TEMP_OFFSET);
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
}
