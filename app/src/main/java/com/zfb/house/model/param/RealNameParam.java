package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 实名认证
 * Created by Administrator on 2016/6/2.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class RealNameParam extends BaseParam {
    private String token;
    private String realName;
    private String identityNo;
    private String photoUrl;
    private String identityUrl;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getIdentityUrl() {
        return identityUrl;
    }

    public void setIdentityUrl(String identityUrl) {
        this.identityUrl = identityUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
