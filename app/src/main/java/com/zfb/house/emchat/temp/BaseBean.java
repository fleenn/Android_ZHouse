package com.zfb.house.emchat.temp;


/**
 * Class Function Instructions:json 返回基类
 *
 * @Version 1.0.0
 * @Author Silence
 */
public class BaseBean {
    /**
     * 请求状态200：成功，1：失败
     */
    public int errorCode = 200;

    /**
     * 返回消息
     */
    public String message = "";
    public boolean isSuccess() {
        return errorCode == 200 ? true : false;
    }
}
