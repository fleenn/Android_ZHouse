package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/6/16.
 */
@Module(server = "zfb_server",name = "order/v2")
public class GrabCustomerListParam extends BaseParam{
    private String handleType = "2";
    private String token;

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
