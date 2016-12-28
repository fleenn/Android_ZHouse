package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by linwenbing on 16/6/19.
 */
@Module(server = "zfb_server", name = "houseResource/v2")
public class HouseEntrustDeleteSellParam extends BaseParam{
    private String token;
    private String sellHouseIds;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSellHouseIds() {
        return sellHouseIds;
    }

    public void setSellHouseIds(String sellHouseIds) {
        this.sellHouseIds = sellHouseIds;
    }
}
