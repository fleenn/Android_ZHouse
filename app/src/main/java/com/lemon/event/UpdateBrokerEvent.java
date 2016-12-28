package com.lemon.event;

/**
 * 更新经济人信息，包括所属公司、所属门店、
 * Created by Administrator on 2016/8/26.
 */
public class UpdateBrokerEvent {
    private String company;//所属公司
    private String shop;//所属门店
    private String smrzState;//实名认证
    private String zzrzState;//资质认证
    private String serviceDistrictName;//片区名集合，有多个的话用逗号隔开
    private String serviceVillageName;//小区名集合，有多个的话用逗号隔开

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSmrzState() {
        return smrzState;
    }

    public void setSmrzState(String smrzState) {
        this.smrzState = smrzState;
    }

    public String getZzrzState() {
        return zzrzState;
    }

    public void setZzrzState(String zzrzState) {
        this.zzrzState = zzrzState;
    }

    public String getServiceDistrictName() {
        return serviceDistrictName;
    }

    public void setServiceDistrictName(String serviceDistrictName) {
        this.serviceDistrictName = serviceDistrictName;
    }

    public String getServiceVillageName() {
        return serviceVillageName;
    }

    public void setServiceVillageName(String serviceVillageName) {
        this.serviceVillageName = serviceVillageName;
    }
}
