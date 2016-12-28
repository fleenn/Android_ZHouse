package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.model.param]
 * 类描述:    [店铺出租列表]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/6/4 17:26]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/6/4 17:26]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Module(server = "zfb_server", name = "broker/v2", httpMethod = "post")
public class RentListParam extends BaseParam {

    private int pageNo;
    private int pageSize;
    private String token;
    private String brokers;
    private String upDown = "";
    private boolean isRefresh = true;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBrokers() {
        return brokers;
    }

    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    public boolean getIsRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    @Override
    public String toString() {
        return "RentListParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", token='" + token + '\'' +
                ", brokers='" + brokers + '\'' +
                '}';
    }
}
