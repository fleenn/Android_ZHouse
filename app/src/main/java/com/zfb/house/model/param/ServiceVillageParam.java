package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 关注的小区 or 服务的小区
 * Created by Administrator on 2016/5/19.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class ServiceVillageParam extends BaseParam {
    private String token;
    private String serviceVillageIds;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServiceVillageIds() {
        return serviceVillageIds;
    }

    public void setServiceVillageIds(String serviceVillageIds) {
        this.serviceVillageIds = serviceVillageIds;
    }
}
