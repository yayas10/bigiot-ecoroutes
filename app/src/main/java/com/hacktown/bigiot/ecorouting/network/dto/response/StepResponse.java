package com.hacktown.bigiot.ecorouting.network.dto.response;

import com.google.gson.annotations.SerializedName;

public class StepResponse {

    @SerializedName("distance")
    private TextValueResponse distance;

    @SerializedName("duration")
    private TextValueResponse duration;

    @SerializedName("start_location")
    private LatLngResponse startLocation;

    @SerializedName("end_location")
    private LatLngResponse endLocation;

    @SerializedName("polyline")
    private PolylineResponse poyline;

    public TextValueResponse getDistance() {
        return distance;
    }

    public void setDistance(TextValueResponse distance) {
        this.distance = distance;
    }

    public TextValueResponse getDuration() {
        return duration;
    }

    public void setDuration(TextValueResponse duration) {
        this.duration = duration;
    }

    public LatLngResponse getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(
        LatLngResponse startLocation) {
        this.startLocation = startLocation;
    }

    public LatLngResponse getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(
        LatLngResponse endLocation) {
        this.endLocation = endLocation;
    }

    public PolylineResponse getPoyline() {
        return poyline;
    }

    public void setPoyline(PolylineResponse poyline) {
        this.poyline = poyline;
    }
}
