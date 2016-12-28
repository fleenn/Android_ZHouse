package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 好友列表参数
 * Created by Administrator on 2016/5/21.
 */
@Module(server = "zfb_server", name = "freind/v2")
public class SearchFriendsParam extends BaseParam {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
