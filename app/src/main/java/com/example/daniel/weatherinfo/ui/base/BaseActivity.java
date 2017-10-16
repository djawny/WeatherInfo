package com.example.daniel.weatherinfo.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.di.component.ActivityComponent;

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

    public void showSnackBar(String message, int length) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, length);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }
}
