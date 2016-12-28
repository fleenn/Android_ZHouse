package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 发布出租房源
 * Created by linwenbing on 16/6/16.
 */
@Module(server = "zfb_server",name = "houseResource/v2")
public class ReleaseRentHouseParam extends BaseParam {

    private String token;
    private int houseType;
    private String body;
    private String tag;
    private String houseReqStr;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHouseReqStr() {
        return houseReqStr;
    }

    public void setHouseReqStr(String houseReqStr) {
        this.houseReqStr = houseReqStr;
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

    public int getHouseType() {
        return houseType;
    }

    public void setHouseType(int houseType) {
        this.houseType = houseType;
    }

}
