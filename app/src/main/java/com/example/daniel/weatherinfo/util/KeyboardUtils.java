package com.example.daniel.weatherinfo.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardUtils {

    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
