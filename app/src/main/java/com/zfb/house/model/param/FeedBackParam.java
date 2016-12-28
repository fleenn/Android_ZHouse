package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 意见反馈
 * Created by Administrator on 2016/7/12.
 */
@Module(server = "zfb_server", name = "evaluate/v2")
public class FeedBackParam extends BaseParam {
    private String token;
    private String content;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
