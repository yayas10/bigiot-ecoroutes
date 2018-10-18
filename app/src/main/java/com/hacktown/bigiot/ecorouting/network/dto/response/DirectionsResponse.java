package com.hacktown.bigiot.ecorouting.network.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DirectionsResponse {

    @SerializedName("routes")
    private List<RouteResponse> routeList;

    @SerializedName("status")
    private String status;

    public List<RouteResponse> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<RouteResponse> routeList) {
        this.routeList = routeList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
