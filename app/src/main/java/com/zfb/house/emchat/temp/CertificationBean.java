package com.zfb.house.emchat.temp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zzh on 2015-08-29.
 * 认证
 * <p/>
 * update by mrlin on 2016-02-24
 */
public class CertificationBean implements Parcelable, Serializable {
    /**
     * 认证名称
     */
    public String id;
    public String pageM;
    public String isNewRecord;
    public String remarks;
    public String createDate;
    public String updateDate;
    public String userId;
    /**
     * 类型：1、实名认证 2、名片认证 3、资质认证
     */
    public String certificationType;
    /**
     * 名称：1、实名认证 2、名片认证 3、资质认证
     */
    public String certificationName;
    public String picture;
    /**
     * 认证状态
     * 0.未提交
     * 1.等待审核
     * 2.审核通过
     * 3.审核不通过
     */
    public String auditState;
    public String userName;
    public String ext1;
    public String ext2;
    public String ext3;
    public String loginName;
    public String year;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pageM);
        dest.writeString(isNewRecord);
        dest.writeString(remarks);
        dest.writeString(createDate);
        dest.writeString(updateDate);
        dest.writeString(userId);
        dest.writeString(certificationType);
        dest.writeString(certificationName);
        dest.writeString(picture);
        dest.writeString(auditState);
        dest.writeString(userName);
        dest.writeString(ext1);
        dest.writeString(ext2);
        dest.writeString(ext3);
        dest.writeString(loginName);
        dest.writeString(year);
    }

    public static final Creator<CertificationBean> CREATOR = new Creator<CertificationBean>() {
        public CertificationBean createFromParcel(Parcel in) {
            CertificationBean cb = new CertificationBean();
            cb.id = in.readString();
            cb.pageM = in.readString();
            cb.isNewRecord = in.readString();
            cb.remarks = in.readString();
            cb.createDate = in.readString();
            cb.updateDate = in.readString();
            cb.userId = in.readString();
            cb.certificationType = in.readString();
            cb.certificationName = in.readString();
            cb.picture = in.readString();
            cb.auditState = in.readString();
            cb.userName = in.readString();
            cb.ext1 = in.readString();
            cb.ext2 = in.readString();
            cb.ext3 = in.readString();
            cb.loginName = in.readString();
            cb.year = in.readString();
            return cb;
        }

        public CertificationBean[] newArray(int size) {
            return new CertificationBean[size];
        }
    };


    /**
     * {
     "id": "102f4dc5b6204af3b0a4f3111c650063",
     "pageM": null,
     "isNewRecord": false,
     "remarks": null,
     "createDate": "2016-02-24 09:51:15",
     "updateDate": "2016-02-24 09:51:15",
     "userId": "50091f7d1146428797fbf13140757b20",
     "certificationType": 3,
     "certificationName": "资格认证",
     "picture": "http://7xid0d.com1.z0.glb.clouddn.com/pic477971470-457.jpg",
     "auditState": 3,
     "userName": null,
     "ext1": "http://7xid0d.com1.z0.glb.clouddn.com/pic477971470-214.jpg",
     "ext2": "350322199211151083",
     "ext3": "2",
     "loginName": null,
     "year": "5"
     },

     * */
}
