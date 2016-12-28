package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 关注的片区 or 服务的片区
 *
 * Created by Administrator on 2016/5/19.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class ServiceDistrictParam extends BaseParam {
    private String token;
    private String serviceDistrictIds;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServiceDistrictIds() {
        return serviceDistrictIds;
    }

    public void setServiceDistrictIds(String serviceDistrictIds) {
        this.serviceDistrictIds = serviceDistrictIds;
    }
}
