package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 投诉好友
 * Created by Administrator on 2016/7/12.
 */
@Module(server = "zfb_server", name = "evaluate/v2")
public class ComplaintParam extends BaseParam {
    private String token;
    private String userId;
    private String content;
    private String type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
