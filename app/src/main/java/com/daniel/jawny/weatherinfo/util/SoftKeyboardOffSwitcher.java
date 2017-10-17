package com.daniel.jawny.weatherinfo.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class SoftKeyboardOffSwitcher {

    public static void apply(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
