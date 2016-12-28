package com.zfb.house.model.result;

import com.lemon.model.BaseResult;

/**
 * Created by Snekey on 2016/4/24.
 */
public class SmsResult extends BaseResult {
    private String message;
    private String data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
