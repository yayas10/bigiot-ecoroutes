package com.hacktown.bigiot.ecorouting.network.dto.response;

import com.google.gson.annotations.SerializedName;

public class RouteQueryResponse {

    @SerializedName("IniPoint")
    private PointResponse initPoint;

    @SerializedName("EndPoint")
    private PointResponse endPoint;

    @SerializedName("IniBikeStation")
    private InitStationPointResponse iniBikeStation;

    @SerializedName("EndBikeStation")
    private EndStationPointResponse endBikeStation;

    public PointResponse getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(PointResponse initPoint) {
        this.initPoint = initPoint;
    }

    public PointResponse getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(PointResponse endPoint) {
        this.endPoint = endPoint;
    }

    public InitStationPointResponse getIniBikeStation() {
        return iniBikeStation;
    }

    public void setIniBikeStation(
        InitStationPointResponse iniBikeStation) {
        this.iniBikeStation = iniBikeStation;
    }

    public EndStationPointResponse getEndBikeStation() {
        return endBikeStation;
    }

    public void setEndBikeStation(
        EndStationPointResponse endBikeStation) {
        this.endBikeStation = endBikeStation;
    }
}
