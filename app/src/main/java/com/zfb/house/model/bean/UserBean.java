package com.zfb.house.model.bean;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.emchat.temp.BaseBean;
import com.zfb.house.emchat.temp.CertificationBean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBean extends BaseBean implements Serializable {
    public String id;
    public String pageM;
    public boolean isNewRecord;
    public String remarks;
    public String remark;//好友备注
    public String createDate;
    public String updateDate;
    public String companyId;
    public String serviceDistrict;//id，逗号隔开
    public String serviceVillage;//id，逗号隔开
    public String serviceDistrictName;
    public String serviceVillageName;
    public String sellVolume;
    public String rentVolume;
    public String realDegree;
    public String satisfyDegree;
    public String professionalDegree;
    public String office;
    public String loginName;//登录名，默认为手机号码
    public String no;
    public String name;//昵称
    public String email;
    public String phone;
    public String mobile;
    public String userType;//用户类型
    public String loginIp;
    public String loginDate;
    public String loginFlag;
    public String oldLoginName;
    public String newPassword;
    public String oldLoginIp;
    public String oldLoginDate;
    public boolean admin;
    public String roleNames;
    public String sessionId;
    public String photo;
    public int sex;//性别
    public String birthday;//生日
    public String alise;//别名
    public String companyName;//公司名
    public String store;//门店名
    public int pop;
    public String officeName;
    public String password;//密码
    public String sign;// 签名
    public String areaId;// 城市ID
    public String star;//星级
    public String sortTotal;
    public String sortPrise;
    public String range;
    public String role;
    public boolean guanzu;//是否关注该经纪人
    public boolean receiveOrgMsg;
    public boolean receiveBrokerMsg;
    public double lat;
    public double lng;
    public int point;//金币数量
    public String recentStatus;//近期状态（用户）
    public String sid; // 关注id
    public String inviteCode;//邀请码（无用）
    /**
     * 无 0
     * 售房专家 1
     * 租房专家 2
     * 租售专家 3
     */
    public String expertType;//专家类型（经纪人）
    public String nameFullPinyin;//名字全部拼音
    public String solicitideid;
    public String solicitideId;

    /**
     * 未审核    0
     * 等待审核   1
     * 审核通过   2
     * 审核不通过 3
     */
    public String smrzState;//实名认证
    public String zzrzState;//资质认证
    /**
     * 认证
     */
    public ArrayList<CertificationBean> certifications;

    public String getShowName() {
        String nameStr = this.name;
        if (TextUtils.isEmpty(nameStr)) {
            nameStr = this.loginName;
        }
        if (TextUtils.isEmpty(nameStr)) {
            nameStr = this.phone;
        }
        return nameStr;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    private static final String Tag = "UserBean";
    private static UserBean mInstance;

    public static UserBean getInstance(Context context) {
        if (mInstance == null) {
            String temp = SettingUtils.get(context, Tag, "");
            if (!TextUtils.isEmpty(temp)) {
                Type type = new TypeToken<UserBean>() {
                }.getType();
                mInstance = new Gson().fromJson(temp, type);
                if (mInstance == null) {
                    mInstance = new UserBean();
                }
            }
        }
        return mInstance;
    }

    public static void clearUserBean(Context context) {
        mInstance = null;
        SettingUtils.set(context, Tag, "");
    }

    public static void updateUserBean(Context mContext) {
        if (ParamUtils.isNull(mInstance)){
            getInstance(mContext);
        }
        SettingUtils.set(mContext, Tag, mInstance.toString());
    }

    public static void updateUserBean(Context mContext, UserBean userbean) {
        SettingUtils.set(mContext, Tag, userbean == null ? "" : userbean.toString());
        mInstance = userbean;
    }

}
