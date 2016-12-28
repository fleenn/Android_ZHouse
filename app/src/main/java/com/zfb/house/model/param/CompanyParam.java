package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 公司
 * Created by Administrator on 2016/6/1.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class CompanyParam extends BaseParam {
    private String token;
    private int companyId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
