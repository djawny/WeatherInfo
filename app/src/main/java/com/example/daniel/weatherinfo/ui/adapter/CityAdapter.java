package com.example.daniel.weatherinfo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.model.City;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityAdapter extends BaseAdapter<City> {

    public CityAdapter(Context context, List<City> list) {
        super(context, list);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, City item, int position) {

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

        @BindView(R.id.temperature)
        TextView mTemperature;

        @BindView(R.id.description)
        TextView mDescriotion;

        @BindView(R.id.delete_button)
        ImageButton mDeleteButton;


        public CityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
