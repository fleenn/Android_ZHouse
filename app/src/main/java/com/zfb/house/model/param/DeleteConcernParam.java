package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 删除关注的经纪人或房源接口
 * Created by Administrator on 2016/5/6.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class DeleteConcernParam extends BaseParam {
    private String sid;
    private String token;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
