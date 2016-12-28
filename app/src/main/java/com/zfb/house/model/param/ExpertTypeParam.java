package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Administrator on 2016/6/27.
 */
@Module(server = "zfb_server",name = "usercenter/v2")
public class ExpertTypeParam extends BaseParam {
    private String token;
    private String expertType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpertType() {
        return expertType;
    }

    public void setExpertType(String expertType) {
        this.expertType = expertType;
    }
}
