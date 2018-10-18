package com.hacktown.bigiot.ecorouting.storage.model;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class RouteData {

    private String mode;

    private List<LatLng> pointList;

    public RouteData(String mode, List<LatLng> pointList) {
        this.mode = mode;
        this.pointList = pointList;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<LatLng> getPointList() {
        return pointList;
    }

    public void setPointList(List<LatLng> pointList) {
        this.pointList = pointList;
    }
}
