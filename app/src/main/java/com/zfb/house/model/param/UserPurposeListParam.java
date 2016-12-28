package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/10/20.
 */
@Module(server = "zfb_server",name = "order/v2")
public class UserPurposeListParam extends BaseParam{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
