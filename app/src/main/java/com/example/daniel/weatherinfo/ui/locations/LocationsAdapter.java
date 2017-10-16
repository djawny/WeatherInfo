package com.example.daniel.weatherinfo.ui.locations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.data.database.model.City;
import com.example.daniel.weatherinfo.ui.base.BaseAdapter;
import com.example.daniel.weatherinfo.util.BackgroundProvider;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationsAdapter extends BaseAdapter<City> {

    private Boolean mDeleteButtonVisibleFlag;

    public interface OnRecycleViewItemClickListener {

        void deleteClickedItem(int cityId);

        void showClickedItem(int cityId);

        void updateView();
    }

    private OnRecycleViewItemClickListener mListener;

    public LocationsAdapter(Context context, List<City> cities, OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        super(context, cities);
        mListener = onRecycleViewItemClickListener;
        mDeleteButtonVisibleFlag = false;
    }

    public void setDeleteButtonVisibleFlag(Boolean isButtonVisibleFlag) {
        mDeleteButtonVisibleFlag = isButtonVisibleFlag;
        notifyDataSetChanged();
    }

    public Boolean getDeleteButtonVisibleFlag() {
        return mDeleteButtonVisibleFlag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.horizontal_item_view, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, final City city, int position) {
        CityViewHolder cityViewHolder = (CityViewHolder) holder;
        cityViewHolder.bind(city, getContext());

        cityViewHolder.getDeleteButton().setOnClickListener(v -> {
            if (mListener != null) {
                mListener.deleteClickedItem(city.getId());
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            setDeleteButtonVisibleFlag(true);
            mListener.updateView();
            return false;
        });

        holder.itemView.setOnClickListener(v -> {
            if (!getDeleteButtonVisibleFlag()) {
                mListener.showClickedItem(city.getId());
            }
        });

        if (mDeleteButtonVisibleFlag) {
            cityViewHolder.getDeleteButton().setVisibility(View.VISIBLE);
            cityViewHolder.getItemContentLayout().setBackgroundResource(R.color.transparent_black);
        } else {
            cityViewHolder.getDeleteButton().setVisibility(View.GONE);
            cityViewHolder.getItemContentLayout().setBackgroundResource(R.color.transparent);
        }
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_content_layout)
        RelativeLayout mItemContentLayout;

        @BindView(R.id.bg_image_view)
        ImageView mBackground;

        @BindView(R.id.temp)
        TextView mTemperature;

        @BindView(R.id.name)
        TextView mName;

        @BindView(R.id.icon)
        ImageView mIcon;

        @BindView(R.id.delete_button)
        ImageView mDeleteButton;

        public CityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(City city, Context context) {
            Picasso.with(context)
                    .load("http://openweathermap.org/img/w/" + city.getWeather().getIcon() + ".png")
                    .into(mIcon);
            mTemperature.setText(String.format(Locale.getDefault(), "%.1f °C", city.getWeather().getTemp()));
            mName.setText(city.getName());
            mBackground.setImageResource(BackgroundProvider.apply(city.getWeather().getIcon()));
        }

        public RelativeLayout getItemContentLayout() {
            return mItemContentLayout;
        }

        public ImageView getDeleteButton() {
            return mDeleteButton;
        }
    }
}
