package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/4/24.
 * 发送短信验证码
 */
@Module(server = "zfb_server",name = "user/v2",httpMethod = "get")
public class SmsParam extends BaseParam {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
