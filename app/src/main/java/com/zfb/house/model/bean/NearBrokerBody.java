package com.zfb.house.model.bean;

/**
 * Created by Snekey on 2016/6/27.
 */
public class NearBrokerBody {

    private double lat;
    private double lng;
    private String serviceVillage;
    private Long range;

    public Long getRange() {
        return range;
    }

    public void setRange(Long range) {
        this.range = range;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getServiceVillage() {
        return serviceVillage;
    }

    public void setServiceVillage(String serviceVillage) {
        this.serviceVillage = serviceVillage;
    }
}
