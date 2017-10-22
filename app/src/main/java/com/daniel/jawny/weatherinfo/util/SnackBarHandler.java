package com.daniel.jawny.weatherinfo.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class SnackBarHandler {

    public static void show(Activity activity, String message, int duration) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, duration);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
