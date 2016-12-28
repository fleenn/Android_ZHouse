package com.zfb.house.ui;

import android.os.Message;

import com.lemon.LemonContext;
import com.lemon.LemonFragment;
import com.lemon.config.Config;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.RentListParam;
import com.zfb.house.model.param.SellListParam;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.ui]
 * 类描述:    [经纪人店铺出租界面]
 * 创建人:    [xflu]
 * 创建时间:  [2016/6/3 10:11]
 * 修改人:    [xflu]
 * 修改时间:  [2016/6/3 10:11]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public class BrokerShopFragment extends LemonFragment {

    protected String id;
    protected UserBean userBean;
    protected int mSellOrRentType = 0;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public void setBrokerId(String id) {
        this.id = id;
    }

    /**
     * 查询售房列表
     */
    protected void queryShopSellList() {
        SellListParam param = new SellListParam();
        param.setToken(SettingUtils.get(LemonContext.getAppContext(), "token", ""));
        param.setPageNo(1);
        param.setPageSize(Config.getIntValue("list_size"));
        param.setBrokers(ParamUtils.isEmpty(id) ? SettingUtils.get(getActivity(), "id", "") : id);
        apiManager.sellList(param);
    }

    /**
     * 查询租房列表
     */
    protected void queryShopRentList() {
        RentListParam param = new RentListParam();
        param.setToken(SettingUtils.get(LemonContext.getAppContext(), "token", ""));
        param.setPageNo(1);
        param.setPageSize(Config.getIntValue("list_size"));
        param.setBrokers(ParamUtils.isEmpty(id) ? SettingUtils.get(getActivity(), "id", "") : id);
        apiManager.rentList(param);
    }

    @Override
    public void notificationMessage(Message msg) {
        switch (msg.what) {
            case 1:
                queryShopSellList();
                break;
            case 2:
                queryShopRentList();
                break;
        }
    }

}
