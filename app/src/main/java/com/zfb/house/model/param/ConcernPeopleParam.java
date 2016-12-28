package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 查询关注专家接口
 * Created by Administrator on 2016/5/6.
 */
@Module(server = "zfb_server", name = "usercenter/v2")
public class ConcernPeopleParam extends BaseParam {
    private int pageNo;
    private int pageSize;
    private String token;
    private boolean isRefresh;

    public boolean getIsRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

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
}
