package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by linwenbing on 16/6/26.
 */
@Module(server = "zfb_server" , name = "broker/v2" )
public class DeleteShopHouseParam extends BaseParam {
    private String token;
    private String rentingHouseIds;
    private String houseType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRentingHouseIds() {
        return rentingHouseIds;
    }

    public void setRentingHouseIds(String rentingHouseIds) {
        this.rentingHouseIds = rentingHouseIds;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }
}
