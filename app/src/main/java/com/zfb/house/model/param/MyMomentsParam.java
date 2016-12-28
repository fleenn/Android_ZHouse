package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 我的房友圈
 */
@Module(server = "zfb_server", name = "houseElite/v2", httpMethod = "get")
public class MyMomentsParam extends BaseParam {
    private Integer pageNo;//页数
    private Integer pageSize;//每页显示数目
    private String token;

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
