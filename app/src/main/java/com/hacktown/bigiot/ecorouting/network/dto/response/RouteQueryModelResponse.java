package com.hacktown.bigiot.ecorouting.network.dto.response;

import com.google.gson.annotations.SerializedName;

public class RouteQueryModelResponse {

    @SerializedName("Data")
    private RouteQueryResponse dataResponse;

    public RouteQueryResponse getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(
        RouteQueryResponse dataResponse) {
        this.dataResponse = dataResponse;
    }
}
