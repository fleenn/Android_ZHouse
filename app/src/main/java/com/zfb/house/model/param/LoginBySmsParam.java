package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/4/25.
 * 短信登录参数
 */
@Module(server = "zfb_server",name = "user/v2")
public class LoginBySmsParam extends BaseParam{
    private String phone;
    private String smscode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }
}
