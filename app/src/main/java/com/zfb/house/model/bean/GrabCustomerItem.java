package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/6/16.
 */
public class GrabCustomerItem {

    /**
     * customerId : 19260f31b09a439eba566fa0cc0a830b
     * customerLoginName : 18606003060
     * orderId : aec7710f7766476090c0726ef34fb5c9
     * customerName : 庄少
     * customerPohto : http://7xid0d.com1.z0.glb.clouddn.com/pic487497919-101.jpg
     * lat : 24.47816275710122
     * lng : 118.111576022831
     * textMsgs : ["买个豪宅","😇"]
     * voiceMsgs : ["http://7xid0d.com1.z0.glb.clouddn.com/FkPFnosJexKwBPoZGccBKECIJTpV","http://7xid0d.com1.z0.glb.clouddn.com/FkkBnyDkVN6ZN44Lr9i8N1S2NXVb"]
     * id : null
     * brokerage : 1.5%
     * requireType : 4
     */

    private String customerId;
    private String customerLoginName;
    private String orderId;
    private String customerName;
    private String customerPohto;
    private double lat;
    private double lng;
    private Object id;
    private String brokerage;
    private String requireType;
    private List<String> textMsgs;
    private List<String> voiceMsgs;
//    控制点击抢单后按钮变灰字段
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerLoginName() {
        return customerLoginName;
    }

    public void setCustomerLoginName(String customerLoginName) {
        this.customerLoginName = customerLoginName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPohto() {
        return customerPohto;
    }

    public void setCustomerPohto(String customerPohto) {
        this.customerPohto = customerPohto;
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

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(String brokerage) {
        this.brokerage = brokerage;
    }

    public String getRequireType() {
        return requireType;
    }

    public void setRequireType(String requireType) {
        this.requireType = requireType;
    }

    public List<String> getTextMsgs() {
        return textMsgs;
    }

    public void setTextMsgs(List<String> textMsgs) {
        this.textMsgs = textMsgs;
    }

    public List<String> getVoiceMsgs() {
        return voiceMsgs;
    }

    public void setVoiceMsgs(List<String> voiceMsgs) {
        this.voiceMsgs = voiceMsgs;
    }
}
