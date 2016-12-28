package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by linwenbing on 16/6/19.
 */
@Module(server = "zfb_server", name = "order/v2")
public class HouseEntrustCallDeleteParam extends BaseParam{
    private String token;
    private String orderIds;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }
}
