package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 性别
 * Created by Administrator on 2016/6/1.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class SexParam extends BaseParam {
    private String token;
    private int sex;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
