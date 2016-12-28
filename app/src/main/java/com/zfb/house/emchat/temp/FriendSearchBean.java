package com.zfb.house.emchat.temp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2015-10-29.
 */
public class FriendSearchBean extends BaseBean implements Serializable{
    public List<FriendBean> data;

    public class FriendBean{
        public String id;
        public String name = "#";
        public String phone;
        public String mobile;
        public String userType;
        public String photo;
        public String firstLetter;//首字母
        public String nameFullPinyin;
        public String serviceDistrictName;

    }


}
