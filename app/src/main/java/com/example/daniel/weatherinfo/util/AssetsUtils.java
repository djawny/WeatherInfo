package com.example.daniel.weatherinfo.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class AssetsUtils {
    public static String loadJsonFromAssets(String fileName, Context context) throws IOException {
        String json;
        InputStream inputStream = context.getAssets().open(fileName);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        json = new String(buffer, "UTF-8");
        return json;
    }
}
