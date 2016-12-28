package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Administrator on 2016/6/6.
 */
@Module(server = "zfb_server", name = "houseElite/v2")
public class MyMomentDeleteParam extends BaseParam{
    private String token;
    private String eliteId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEliteId() {
        return eliteId;
    }

    public void setEliteId(String eliteId) {
        this.eliteId = eliteId;
    }
}
