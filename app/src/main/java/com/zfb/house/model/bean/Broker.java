package com.zfb.house.model.bean;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lemon.util.SettingUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 经纪人实体
 * Created by Administrator on 2016/5/7.
 */
public class Broker {
    public String id;//用户ID
    public String pageM;
    public Boolean isNewRecord;
    public String remarks;//备注
    public String createDate;
    public String updateDate;
    public String companyId;//所属公司的ID
    public String companyName;//所属公司
    public String areaId;
    public String serviceDistrict;
    public String serviceDistrictName;//服务片区
    public String serviceVillage;
    public String serviceVillageName;//服务小区
    public int sellVolume;
    public int rentVolume;
    public int realDegree;
    public double preLat;
    public double preLng;
    public int satisfyDegree;//好评率
    public String professionalDegree;
    public String office;
    public String loginName;//用户名
    public String no;
    public String name;//姓名
    public String email;
    public String phone;//手机号码
    public String mobile;
    public String userType;//用户类型
    public String loginIp;//用户ID
    public String loginDate;//登录时间
    public String loginFlag;//登录标记
    public String photo;//头像
    public String oldLoginName;//上一次用户名
    public String newPassword;//新密码
    public String oldLoginIp;//上一次登录IP
    public String oldLoginDate;//上一次登录时间
    public String sessionId;
    public int sex;//性别
    public String birthday;//生日
    public String star;//星级
    public String alise;//别名
    public int pop;
    public String officeName;
    public String sign;//个性签名
    public String smrzState;
    public String zzrzState;
    public double lat;//纬度
    public double lng;//经度
    public String sortTotal;
    public String sortPrise;
    public String range;
    public List<BrokerCertifications> certifications = new ArrayList();//证书
    public String role;
    public String roleNames;
    public Boolean receiveOrgMsg;
    public Boolean receiveBrokerMsg;//是否收到经纪人信息
    public Boolean guanzu;//是否关注
    public Boolean admin;
    public String nameFullPinyin;//名字全部拼音
    public int point;//金币数量
    public String solicitideid;
    public String password;
    public int sortVolume;
    public int sortPraise;
    public String solicitideId;
    public int sortPop;
    public double lon;
    public int delFlag;
    public String expertType;//专家类型
     public String recentStatus;//近期状态（用户）

    public Boolean isEdit;//判断是否要进入编辑状态

    private static final String Tag = "Broker";
    private static Broker mInstance;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Broker getInstance(Context context) {
        if (mInstance == null) {
            String temp = SettingUtils.get(context, Tag, "");
            if (!TextUtils.isEmpty(temp)) {
                mInstance = new Gson().fromJson(temp, Broker.class);
                if (mInstance == null) {
                    mInstance = new Broker();
                }
            }
        }
        return mInstance;
    }

    public static void saveBroker(Context context, String json) {
        SettingUtils.set(context,Tag,json);
    }

    public static void updateBroker(Context mContext, Broker broker) {
        SettingUtils.set(mContext, Tag, broker == null ? "" : broker.toString());
        mInstance = broker;
    }


}
