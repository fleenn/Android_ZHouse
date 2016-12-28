package com.zfb.house.init;

import android.content.Context;

import com.lemon.LemonCacheManager;
import com.lemon.LemonContext;
import com.lemon.config.Config;
import com.lemon.init.AbstractInitializer;
import com.lemon.model.StatusCode;
import com.lemon.net.ApiManager;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.MyFriendsParam;
import com.zfb.house.model.result.MyFriendsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snekey on 2016/6/24.
 */
public class FriendsListInitializer extends AbstractInitializer {

    public Context mContext;
    LemonCacheManager cacheManager;
    private MyFriendsParam myFriendsParam;
    ApiManager apiManager;

    @Override
    public Object initialize(Object... objects) throws Exception {
        EventUtil.register(this);
        String token = SettingUtils.get(mContext, "token", null);
        if (!ParamUtils.isNull(token)) {
            getFriendList(token);
        }
        return null;
    }

    /**
     * 切换用户的时候由于不启动初始化器，所以需要重新获取一次好友列表
     * @param token
     */
    public void getFriendListByLogin(String token){
        EventUtil.register(this);
        getFriendList(token);
    }

    public void getFriendList(String token) {
        apiManager = LemonContext.getBean(ApiManager.class);
        myFriendsParam = new MyFriendsParam();
        myFriendsParam.setToken(token);
        myFriendsParam.setUserType(Config.getValue("userFriends"));
        cacheManager = LemonContext.getBean(LemonCacheManager.class);
        apiManager.listMyFriends(myFriendsParam);
    }

    public void onEventAsync(MyFriendsResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            List<UserBean> data = result.getData();
            ArrayList<String> friendList = new ArrayList<>();
            for (UserBean bean : data) {
                friendList.add(bean.id);
            }
            String userType = ((MyFriendsParam) result.getParam()).getUserType();
            if (userType.equals(Config.getValue("userFriends"))) {
                cacheManager.putBean(Config.getValue("userFriends"), friendList);
                myFriendsParam.setUserType(Config.getValue("brokerFriends"));
                apiManager.listMyFriends(myFriendsParam);
            } else {
                cacheManager.putBean(Config.getValue("brokerFriends"), friendList);
                EventUtil.unregister(this);
            }
        }
    }
}
