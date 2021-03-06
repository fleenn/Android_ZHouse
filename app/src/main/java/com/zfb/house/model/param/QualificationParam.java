package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 资质认证参数
 * Created by Administrator on 2016/6/27.
 */
@Module(server = "zfb_server", name = "broker/v2")
public class QualificationParam extends BaseParam {
    private String token;
    private String body;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
