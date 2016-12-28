package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;
import com.zfb.house.model.bean.ReplyContent;

/**
 * Created by Snekey on 2016/5/28.
 */
@Module(server = "zfb_server" , name = "houseElite/v2" )
public class DeleteReplyParam extends BaseParam{
    private String replyId;
    private String token;
    private int position;
    private ReplyContent replyContent;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ReplyContent getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(ReplyContent replyContent) {
        this.replyContent = replyContent;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
