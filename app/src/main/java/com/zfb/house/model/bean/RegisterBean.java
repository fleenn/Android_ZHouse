package com.zfb.house.model.bean;

/**
 * Created by Administrator on 2016/7/4.
 */
public class RegisterBean {

    private UserBean user;
    private String token;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
