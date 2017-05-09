package com.example.daniel.weatherinfo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    private final List<T> mItems = new ArrayList<>();
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    public abstract void onBind(RecyclerView.ViewHolder holder, T item, int position);

    public BaseAdapter(Context context) {
        this(context, null);
    }

    public BaseAdapter(Context context, List<T> list) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null!!!");
        }
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        if (list != null) {
            mItems.addAll(list);
        }
    }

    @Override
    public int getItemCount() {
        int size = mItems.size();
        return size;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final T item = getItem(position);
        onBind(holder, item, position);
    }

    public final Context getContext() {
        return mContext;
    }

    public final LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    public T getItem(int position) {
        final T item = mItems.get(position);
        return item;
    }

    public void swapData(List<T> items) {
        if (items != null) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        mItems.clear();
        notifyDataSetChanged();
    }

}
