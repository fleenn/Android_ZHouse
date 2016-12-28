package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/6/30.
 */
@Module(server = "zfb_server", name = "broker/v2")
public class BrokerListParam extends BaseParam {

    private int pageNo;
    private int pageSize = 10;
    private int brokerType;
    private String regionId;
    private String serviceDistrictId ;
    private String communtityId ;
    private int orderType;//3好评 4成交量 5星级
    private boolean isRefresh = true;

    public boolean getIsRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public int getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(int brokerType) {
        this.brokerType = brokerType;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getServiceDistrictId() {
        return serviceDistrictId;
    }

    public void setServiceDistrictId(String serviceDistrictId) {
        this.serviceDistrictId = serviceDistrictId;
    }

    public String getCommuntityId() {
        return communtityId;
    }

    public void setCommuntityId(String communtityId) {
        this.communtityId = communtityId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
