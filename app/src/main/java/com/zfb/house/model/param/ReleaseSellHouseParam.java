package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 发布出售房源
 * Created by linwenbing on 16/6/16.
 */
@Module(server = "zfb_server", name = "houseResource/v2")
public class ReleaseSellHouseParam extends BaseParam {

    private String token;
    private int houseType;
    private String body;
    private String tag;
    private String houseDealReqStr;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHouseDealReqStr() {
        return houseDealReqStr;
    }

    public void setHouseDealReqStr(String houseDealReqStr) {
        this.houseDealReqStr = houseDealReqStr;
    }

    public int getHouseType() {
        return houseType;
    }

    public void setHouseType(int houseType) {
        this.houseType = houseType;
    }

}
