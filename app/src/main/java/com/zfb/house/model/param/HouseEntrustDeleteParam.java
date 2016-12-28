package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by linwenbing on 16/6/19.
 */
@Module(server = "zfb_server", name = "houseResource/v2")
public class HouseEntrustDeleteParam extends BaseParam{
    private String token;
    private String rentingHouseIds;

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
}
