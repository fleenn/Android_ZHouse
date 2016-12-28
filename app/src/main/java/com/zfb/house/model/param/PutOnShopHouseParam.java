package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by linwenbing on 16/6/26.
 */
@Module(server = "zfb_server" , name = "broker/v2" )
public class PutOnShopHouseParam extends BaseParam {
    private String token;
    private String houseId;
    private String houseType;
    private String upDown;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }
}
