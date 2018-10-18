package com.hacktown.bigiot.ecorouting.network.dto.response;

import com.google.gson.annotations.SerializedName;

public class BoundsResponse {

    @SerializedName("northeast")
    private LatLngResponse northeast;

    @SerializedName("southwest")
    private LatLngResponse southwest;
}
