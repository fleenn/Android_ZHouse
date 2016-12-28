package com.zfb.house.model.bean;

/**
 * Created by Snekey on 2016/6/24.
 */
public class LoginBean {
    private String token;
    private UserBean user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
