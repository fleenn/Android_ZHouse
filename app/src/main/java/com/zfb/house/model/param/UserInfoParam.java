package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 用户信息
 * Created by Administrator on 2016/5/31.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class UserInfoParam extends BaseParam {
    private String token;
    private String serviceDistrictIds;
    private String serviceVillageIds;
    private String photoUrl;
    private String alise;
    private Integer sex;
    private String mobile;
    private String age;
    private String sign;//签名
    private String company;//公司
    private String store;//门店
    private String recentStatus;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServiceDistrictIds() {
        return serviceDistrictIds;
    }

    public void setServiceDistrictIds(String serviceDistrictIds) {
        this.serviceDistrictIds = serviceDistrictIds;
    }

    public String getServiceVillageIds() {
        return serviceVillageIds;
    }

    public void setServiceVillageIds(String serviceVillageIds) {
        this.serviceVillageIds = serviceVillageIds;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAlise() {
        return alise;
    }

    public void setAlise(String alise) {
        this.alise = alise;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getRecentStatus() {
        return recentStatus;
    }

    public void setRecentStatus(String recentStatus) {
        this.recentStatus = recentStatus;
    }
}
