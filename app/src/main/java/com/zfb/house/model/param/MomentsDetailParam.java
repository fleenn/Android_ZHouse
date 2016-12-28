package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/7/5.
 */
@Module(server = "zfb_server",name = "houseElite/v2",httpMethod = "get")
public class MomentsDetailParam extends BaseParam{
    private String eliteId;
    private String token;

    public String getEliteId() {
        return eliteId;
    }

    public void setEliteId(String eliteId) {
        this.eliteId = eliteId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
