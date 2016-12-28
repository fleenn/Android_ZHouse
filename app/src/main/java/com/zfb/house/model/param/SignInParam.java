package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 签到
 * Created by Administrator on 2016/5/31.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class SignInParam extends BaseParam {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
