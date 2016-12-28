package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Administrator on 2016/8/8.
 */
@Module(server = "zfb_server",name = "mall/v2")
public class GoodsListParam extends BaseParam {
    private String token;
    private Integer pageNo;
    private Integer pageSize;
    private String isRecommand;
    private boolean isRefresh;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getIsRecommand() {
        return isRecommand;
    }

    public void setIsRecommand(String isRecommand) {
        this.isRecommand = isRecommand;
    }

    public boolean getIsRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
