package com.hacktown.bigiot.ecorouting.network.dto.request;

import com.google.gson.annotations.SerializedName;

public class RouteQueryModelRequest {

    @SerializedName("ini_point")
    private PointRequest iniPoint;

    @SerializedName("end_point")
    private PointRequest endPoint;

    public PointRequest getIniPoint() {
        return iniPoint;
    }

    public void setIniPoint(PointRequest iniPoint) {
        this.iniPoint = iniPoint;
    }

    public PointRequest getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(PointRequest endPoint) {
        this.endPoint = endPoint;
    }
}
