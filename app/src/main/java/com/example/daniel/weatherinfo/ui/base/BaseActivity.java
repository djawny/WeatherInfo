package com.example.daniel.weatherinfo.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.daniel.weatherinfo.MyApplication;
import com.example.daniel.weatherinfo.R;
import com.example.daniel.weatherinfo.di.component.ActivityComponent;
import com.example.daniel.weatherinfo.di.component.DaggerActivityComponent;
import com.example.daniel.weatherinfo.di.mudule.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        initializeActivityComponent();
    }

    public void initializeActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(MyApplication.getComponent(this))
                .activityModule(new ActivityModule(this))
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    public void showSnackBar(String message, int length) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, length);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    public void hideStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
