package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 查询收藏的租房房源
 * Created by Administrator on 2016/5/27.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class CollectRentParam extends BaseParam {
    private Integer pageNo;
    private Integer pageSize;
    private String token;
    private boolean isRefresh;

    public boolean getIsRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
