package com.daniel.jawny.weatherinfo.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.daniel.jawny.weatherinfo.di.component.ActivityComponent;

public abstract class BaseFragment extends Fragment {
    private ActivityComponent mActivityComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeActivityComponent();
    }

    public void initializeActivityComponent() {
        mActivityComponent = ActivityComponent.Initializer.init(getActivity());
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
