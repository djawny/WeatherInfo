package com.daniel.jawny.weatherinfo.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.daniel.jawny.weatherinfo.R;
import com.daniel.jawny.weatherinfo.di.component.ActivityComponent;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeActivityComponent();
    }

    public void initializeActivityComponent() {
        mActivityComponent = ActivityComponent.Initializer.init(this);
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
