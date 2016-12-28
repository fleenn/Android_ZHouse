package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 更新头像
 * Created by Administrator on 2016/6/2.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class AvatarParam extends BaseParam {
    private String token;
    private String photoUrl;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
