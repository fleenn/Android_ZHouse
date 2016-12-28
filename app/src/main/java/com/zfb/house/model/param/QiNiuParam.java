package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;
import com.zfb.house.model.bean.ChatMessage;

/**
 * Created by Snekey on 2016/5/13.
 * 获取七牛token
 */
@Module(server = "zfb_old_server",name = "a/c")
public class QiNiuParam extends BaseParam{
    private ChatMessage message;
    private String filePath;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
