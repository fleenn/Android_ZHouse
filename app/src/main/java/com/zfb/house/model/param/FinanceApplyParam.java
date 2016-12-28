package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Administrator on 2016/7/19.
 */
@Module(server = "zfb_server", name = "finace/v2")
public class FinanceApplyParam extends BaseParam {
    private String token;
    private String userName;
    private String mobile;
    private String purposeVillage;
    private String type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPurposeVillage() {
        return purposeVillage;
    }

    public void setPurposeVillage(String purposeVillage) {
        this.purposeVillage = purposeVillage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
