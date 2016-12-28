package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/6/18.
 */
@Module(server = "zfb_server",name = "houseResource/v2")
public class GrabHouseListParam extends BaseParam{
    private String token;
    private int pageNo;
    private int pageSize = 10;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
