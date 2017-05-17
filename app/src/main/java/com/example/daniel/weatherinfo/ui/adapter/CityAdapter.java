package com.example.daniel.weatherinfo.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityAdapter extends BaseAdapter<City> {

    private Boolean mIsButtonVisibleFlag;

    public interface OnCityCrossClickedListener {
        void onDelete(int cityId);
    }

    private OnCityCrossClickedListener mListener;

    public CityAdapter(Context context, List<City> list, OnCityCrossClickedListener onCityCrossClickedListener) {
        super(context, list);
        mListener = onCityCrossClickedListener;
        mIsButtonVisibleFlag = false;
    }

    public void setIsButtonVisibleFlag(Boolean mIsButtonVisibleFlag) {
        this.mIsButtonVisibleFlag = mIsButtonVisibleFlag;
        notifyDataSetChanged();
    }

    public Boolean getIsButtonVisibleFlag() {
        return mIsButtonVisibleFlag;
    }

    @Override
    public void onBind(final RecyclerView.ViewHolder holder, final City city, int position) {
        CityHolder cityHolder = (CityHolder) holder;
        cityHolder.bind(city, getContext());

        cityHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDelete(city.getId());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setIsButtonVisibleFlag(true);
                return false;
            }
        });

        if (mIsButtonVisibleFlag) {
            cityHolder.getDeleteButton().setVisibility(View.VISIBLE);
            cityHolder.getContentLayout().setBackgroundResource(R.color.transparent_black);
        } else {
            cityHolder.getDeleteButton().setVisibility(View.GONE);
            cityHolder.getContentLayout().setBackgroundResource(R.color.transparent);
        }
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

        @BindView(R.id.content_layout)
        RelativeLayout mContentLayout;


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

        public ImageView getDeleteButton() {
            return mDeleteButton;
        }

        public RelativeLayout getContentLayout() {
            return mContentLayout;
        }
    }
}
