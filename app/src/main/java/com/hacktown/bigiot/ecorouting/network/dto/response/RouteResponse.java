package com.hacktown.bigiot.ecorouting.network.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RouteResponse {

    @SerializedName("bounds")
    private BoundsResponse bounds;

    @SerializedName("legs")
    private List<LegResponse> legList;

    public BoundsResponse getBounds() {
        return bounds;
    }

    public void setBounds(BoundsResponse bounds) {
        this.bounds = bounds;
    }

    public List<LegResponse> getLegList() {
        return legList;
    }

    public void setLegList(
        List<LegResponse> legList) {
        this.legList = legList;
    }
}
