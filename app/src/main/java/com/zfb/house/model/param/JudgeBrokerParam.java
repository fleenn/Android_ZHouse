package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by linwenbing on 16/7/26.
 */
@Module(server = "zfb_server", name = "evaluate/v2/")
public class JudgeBrokerParam extends BaseParam {
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
