package com.example.daniel.weatherinfo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityAdapter extends BaseAdapter<City> {

    public interface OnCityCrossClickedListener {
        void onDelete(int cityId);
    }

    private OnCityCrossClickedListener mListener;

    public CityAdapter(Context context, List<City> list, OnCityCrossClickedListener onCityCrossClickedListener) {
        super(context, list);
        mListener = onCityCrossClickedListener;
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, final City city, int position) {
        ((CityHolder) holder).bind(city, getContext());
        ((CityHolder) holder).mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDelete(city.getId());
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.recycle_view_raw_layout, parent, false);
        return new CityHolder(view);
    }

    public static class CityHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        ImageView mIcon;

        @BindView(R.id.name_country)
        TextView mNameCountry;

        @BindView(R.id.temp)
        TextView mTemperature;

        @BindView(R.id.description)
        TextView mDescription;

        @BindView(R.id.delete_button)
        ImageView mDeleteButton;


        public CityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(City city, Context context) {
            Picasso.with(context)
                    .load("http://openweathermap.org/img/w/" + city.getWeather().getIcon() + ".png")
                    .into(mIcon);
            mNameCountry.setText(String.format("%s, %s", city.getName(), city.getCountry()));
            mTemperature.setText(String.format("%s °C", String.valueOf(city.getWeather().getTemp())));
            mDescription.setText(city.getWeather().getDescription());
        }
    }
}
