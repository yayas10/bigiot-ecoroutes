package com.hacktown.bigiot.ecorouting.network.dto.response;

import com.google.gson.annotations.SerializedName;

public class PolylineResponse {

    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
