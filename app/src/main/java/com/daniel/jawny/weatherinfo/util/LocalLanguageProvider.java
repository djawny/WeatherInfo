package com.daniel.jawny.weatherinfo.util;

import java.util.Locale;

public final class LocalLanguageProvider {

    public static String apply() {
        String language = Locale.getDefault().getLanguage();
        switch (language) {
            case "pl":
                return "pl";
            default:
                return "en";
        }
    }
}
