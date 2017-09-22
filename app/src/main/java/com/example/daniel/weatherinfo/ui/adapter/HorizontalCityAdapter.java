package com.example.daniel.weatherinfo.ui.adapter;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class HorizontalCityAdapter extends BaseAdapter<City> {

    private Boolean mIsButtonVisibleFlag;

    public interface OnRecycleViewItemClickListener {

        void deleteClickedItem(int cityId);

        void showClickedItem(int cityId);

        void updateView();
    }

    private OnRecycleViewItemClickListener mListener;

    public HorizontalCityAdapter(Context context, List<City> cities, OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        super(context, cities);
        mListener = onRecycleViewItemClickListener;
        mIsButtonVisibleFlag = false;
    }

    public void setIsButtonVisibleFlag(Boolean isButtonVisibleFlag) {
        mIsButtonVisibleFlag = isButtonVisibleFlag;
        notifyDataSetChanged();
    }

    public Boolean getIsButtonVisibleFlag() {
        return mIsButtonVisibleFlag;
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

        cityViewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.deleteClickedItem(city.getId());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setIsButtonVisibleFlag(true);
                mListener.updateView();
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getIsButtonVisibleFlag()) {
                    mListener.showClickedItem(city.getId());
                }
            }
        });

        if (mIsButtonVisibleFlag) {
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
            mTemperature.setText(String.format("%s °C", String.valueOf(city.getWeather().getTemp())));
            mName.setText(city.getName());
            mBackground.setImageResource(BackgroundProvider.getBackground(city.getWeather().getIcon()));
        }

        public RelativeLayout getItemContentLayout() {
            return mItemContentLayout;
        }

        public ImageView getDeleteButton() {
            return mDeleteButton;
        }
    }
}
