package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 关注专家接口
 * Created by Administrator on 2016/5/6.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class ConcernParam extends BaseParam {
    private String solicitideId;
    private String token;

    public String getSolicitideId() {
        return solicitideId;
    }

    public void setSolicitideId(String solicitideId) {
        this.solicitideId = solicitideId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
