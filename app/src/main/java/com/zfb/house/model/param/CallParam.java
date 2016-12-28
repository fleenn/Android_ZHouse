package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;
import com.zfb.house.model.bean.ChatMessage;

/**
 * Created by Snekey on 2016/6/12.
 */
@Module(server = "zfb_server",name = "order/v2")
public class CallParam extends BaseParam{
    private double lat;
    private double lng;
    private String textMsg;
    private String voiceMsg;
    private String brokerage;
    private String requireType;
    private String token;
    private ChatMessage message;

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getVoiceMsg() {
        return voiceMsg;
    }

    public void setVoiceMsg(String voiceMsg) {
        this.voiceMsg = voiceMsg;
    }

    public String getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(String brokerage) {
        this.brokerage = brokerage;
    }

    public String getRequireType() {
        return requireType;
    }

    public void setRequireType(String requireType) {
        this.requireType = requireType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
