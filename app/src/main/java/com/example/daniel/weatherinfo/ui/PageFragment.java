package com.example.daniel.weatherinfo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.data.database.model.Forecast;
import com.example.daniel.weatherinfo.data.database.model.MyBarChart;
import com.example.daniel.weatherinfo.data.database.model.MyLineChart;
import com.example.daniel.weatherinfo.ui.adapter.CustomXAxisValueFormatter;
import com.example.daniel.weatherinfo.ui.base.BaseFragment;
import com.example.daniel.weatherinfo.util.AppConstants;
import com.example.daniel.weatherinfo.util.DateUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.daniel.weatherinfo.util.AppConstants.CHART_TEMP_OFFSET;

public class PageFragment extends BaseFragment implements PageFragmentView {

    private static final String ARG_CITY_ID = "city_id";

    @BindView(R.id.city_country)
    TextView mCityCountry;

    @BindView(R.id.icon)
    ImageView mIcon;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.temp)
    TextView mTemperature;

//    @BindView(R.id.temperature_range)
//    TextView mTemperatureRange;

    @BindView(R.id.wind)
    TextView mWind;

    @BindView(R.id.cloudiness)
    TextView mCloudiness;

    @BindView(R.id.pressure)
    TextView mPressure;

    @BindView(R.id.sunrise)
    TextView mSunrise;

    @BindView(R.id.sunset)
    TextView mSunset;

    @BindView(R.id.bg_image_view)
    ImageView mBackground;

    @BindView(R.id.line_chart)
    LineChart mLineChart;

    @BindView(R.id.bar_chart)
    BarChart mBarChart;

    @Inject
    PageFragmentPresenter mPresenter;

    public PageFragment() {
    }

    public static PageFragment newInstance(int cityId) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITY_ID, cityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivityComponent().inject(this);
        initializePresenter();
        int cityId = getArguments().getInt(ARG_CITY_ID);
        loadCity(cityId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.clearDisposable();
    }

    private void initializePresenter() {
        mPresenter.setView(this);
    }

    private void loadCity(int cityId) {
        mPresenter.loadCityFromDatabase(cityId);
    }

    @Override
    public void displayCity(City city) {
        String icon = city.getWeather().getIcon();
        Picasso.with(getActivity())
                .load("http://openweathermap.org/img/w/" + icon + ".png")
                .into(mIcon);
        setBackground(icon);
        mCityCountry.setText(String.format("%s, %s", city.getName(), city.getCountry()));
        String date = DateUtils.getDateFromUTCTimestamp(city.getWeather().getDate(), AppConstants.DATE_NEW_LINE_TIME);
        mDate.setText(date);
        mDescription.setText(city.getWeather().getDescription());
        mTemperature.setText(String.format("%s °C", String.valueOf(city.getWeather().getTemp())));
//        mTemperatureRange.setText(String.format("Temperature from %s °C to %s °C", city.getWeather().getTempMin(), city.getWeather().getTempMax()));
        mWind.setText(String.format("Wind: %s m/s", city.getWeather().getWindSpeed()));
        mCloudiness.setText(String.format("Cloudiness: %s %%", city.getWeather().getCloudiness()));
        mPressure.setText(String.format("Pressure: %s hpa", city.getWeather().getPressure()));
        String sunrise = DateUtils.getDateFromUTCTimestamp(city.getWeather().getSunrise(), AppConstants.TIME);
        mSunrise.setText(String.format("Sunrise: %s", sunrise));
        String sunset = DateUtils.getDateFromUTCTimestamp(city.getWeather().getSunset(), AppConstants.TIME);
        mSunset.setText(String.format("Sunset: %s", sunset));
        List<Forecast> forecasts = city.getForecasts();
        drawLineChart(forecasts);
        drawBarChart(forecasts);
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
        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "°C";
            }
        });

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

        BarDataSet barDataSet = new BarDataSet(chart.getEntries(), "Forecast");
        BarData barData = new BarData(barDataSet);
        barData.setValueTextSize(16);
        barData.setHighlightEnabled(false);
        barData.setValueTextColor(Color.WHITE);
        barData.setBarWidth(0.5f);
        barData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "°C";
            }
        });

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

    private void setBackground(String icon) {
        switch (icon) {
            case "01d": //clear sky
                mBackground.setImageResource(R.drawable.day_noclouds);
                break;
            case "02d": //few clouds
            case "03d": //scattered clouds
            case "04d": //broken clouds
            case "50d": //mist
                mBackground.setImageResource(R.drawable.day_clouds);
                break;
            case "09d": //shower rain
            case "10d": //rain
            case "11d": //thunderstorm
                mBackground.setImageResource(R.drawable.day_rain);
                break;
            case "13d": //snow
                mBackground.setImageResource(R.drawable.day_snow);
                break;
            case "01n":
                mBackground.setImageResource(R.drawable.night_noclouds);
                break;
            case "02n":
            case "03n":
            case "04n":
            case "50n":
                mBackground.setImageResource(R.drawable.night_clouds);
                break;
            case "09n":
            case "10n":
            case "11n":
                mBackground.setImageResource(R.drawable.night_rain);
                break;
            case "13n":
                mBackground.setImageResource(R.drawable.night_snow);
                break;
            default:
                mBackground.setImageResource(R.drawable.day_noclouds);
        }
    }
}
