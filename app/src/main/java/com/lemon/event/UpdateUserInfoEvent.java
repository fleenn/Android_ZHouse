package com.lemon.event;

/**
 * 更新经纪人或者用户个人信息，包括头像、昵称、性别
 * Created by HourGlassRemember on 2016/8/25.
 */
public class UpdateUserInfoEvent {
    private String photo;//头像
    private String name;//昵称
    private int sex;//性别

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

}
