package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 更新别名
 * Created by Administrator on 2016/6/1.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class AliasParam extends BaseParam {
    private String token;
    private String alise;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAlise() {
        return alise;
    }

    public void setAlise(String alise) {
        this.alise = alise;
    }
}
