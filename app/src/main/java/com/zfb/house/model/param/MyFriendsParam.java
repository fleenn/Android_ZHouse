package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 好友列表参数
 * Created by Administrator on 2016/5/21.
 */
@Module(server = "zfb_server", name = "freind/v2")
public class MyFriendsParam extends BaseParam {
    private String token;
    private String userType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
