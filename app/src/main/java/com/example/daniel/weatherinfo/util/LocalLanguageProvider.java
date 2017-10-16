package com.example.daniel.weatherinfo.util;

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
