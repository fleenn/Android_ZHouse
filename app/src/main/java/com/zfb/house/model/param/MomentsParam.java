package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/5/17.
 * 附近房友圈
 */
@Module(server = "zfb_server",name = "houseElite/v2",httpMethod = "get")
public class MomentsParam extends BaseParam{
    private Integer pageNo;
    private Integer pageSize;
    private String lat;
    private String lng;
    private long range;
    private String token;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public long getRange() {
        return range;
    }

    public void setRange(long range) {
        this.range = range;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
