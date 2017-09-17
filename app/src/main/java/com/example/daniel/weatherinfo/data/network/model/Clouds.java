package com.example.daniel.weatherinfo.data.network.model;

import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    private int cloudiness;

    public int getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(int cloudiness) {
        this.cloudiness = cloudiness;
    }
}
