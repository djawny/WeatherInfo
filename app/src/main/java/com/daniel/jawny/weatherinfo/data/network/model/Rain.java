package com.daniel.jawny.weatherinfo.data.network.model;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private double volume;

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
