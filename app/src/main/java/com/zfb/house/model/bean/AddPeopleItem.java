package com.zfb.house.model.bean;

/**
 * Created by linwenbing on 16/7/6.
 */
public class AddPeopleItem {

    /**
     * id : 173a5169cf5d400abceb78fdbb15140c
     * name : 何亮亮
     * phone : 18750273454
     * mobile : 18750273454
     * userType : 1
     * photo : null
     * serviceDistrictName : 禾祥东片区,禾祥东片区,禾祥东片区
     */

    private String id;
    private String name;
    private String phone;
    private String mobile;
    private String userType;
    private String photo;
    private String serviceDistrictName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getServiceDistrictName() {
        return serviceDistrictName;
    }

    public void setServiceDistrictName(String serviceDistrictName) {
        this.serviceDistrictName = serviceDistrictName;
    }
}
