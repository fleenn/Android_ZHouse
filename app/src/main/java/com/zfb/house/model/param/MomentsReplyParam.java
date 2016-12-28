package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/5/27.
 */
@Module(server = "zfb_server",name = "houseElite/v2")
public class MomentsReplyParam extends BaseParam{
    private String eliteId;
    private String replyContent;
    private String replyUserId;
    private String token;
    private int position;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEliteId() {
        return eliteId;
    }

    public void setEliteId(String eliteId) {
        this.eliteId = eliteId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
