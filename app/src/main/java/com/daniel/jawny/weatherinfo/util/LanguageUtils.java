package com.daniel.jawny.weatherinfo.util;

import java.util.Locale;

public final class LanguageUtils {

    public static String getLocalLang() {
        String language = Locale.getDefault().getLanguage();
        switch (language) {
            case "pl":
                return "pl";
            default:
                return "en";
        }
    }
}
