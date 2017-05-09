package com.example.daniel.weatherinfo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.daniel.weatherinfo.model.City;

import java.util.List;

public class CityAdapter extends BaseAdapter<City>{

    public CityAdapter(Context context, List<City> list) {
        super(context, list);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, City item, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    public static class CityHolder extends RecyclerView.ViewHolder{

        public CityHolder(View itemView) {
            super(itemView);
        }
    }
}
