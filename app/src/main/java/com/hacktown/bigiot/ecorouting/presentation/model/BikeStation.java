package com.hacktown.bigiot.ecorouting.presentation.model;

public class BikeStation {

    private Integer slots;

    private Integer bikes;

    private Double latitude;

    private String type;

    private Double longitude;

    private String status;

    public Integer getSlots() {
        return slots;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }

    public Integer getBikes() {
        return bikes;
    }

    public void setBikes(Integer bikes) {
        this.bikes = bikes;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
