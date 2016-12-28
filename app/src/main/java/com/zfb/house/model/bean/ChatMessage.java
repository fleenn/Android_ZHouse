package com.zfb.house.model.bean;

/**
 * Created by Snekey on 2016/6/11.
 */
public class ChatMessage {
    public int type;//0,txt,1 voice
    public String content;//txt, or file path for voice
    public int status = 0;//0,loading,1 success,2 failed
    public int length;
    public String photoPath;//photo path;
}
