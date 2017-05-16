package com.example.daniel.weatherinfo.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.di.component.ActivityComponent;
import com.example.daniel.weatherinfo.di.component.DaggerActivityComponent;
import com.example.daniel.weatherinfo.di.mudule.ActivityModule;

public abstract class BaseFragment extends Fragment {

    private ActivityComponent mActivityComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeActivityComponent();
    }

    public void initializeActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(MyApplication.getComponent(getActivity()))
                .activityModule(new ActivityModule(getActivity()))
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
