package com.hacktown.bigiot.ecorouting.network.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LegResponse {

    @SerializedName("distance")
    private TextValueResponse distance;

    @SerializedName("duration")
    private TextValueResponse duration;

    @SerializedName("steps")
    private List<StepResponse> stepList;

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

    public List<StepResponse> getStepList() {
        return stepList;
    }

    public void setStepList(List<StepResponse> stepList) {
        this.stepList = stepList;
    }
}
