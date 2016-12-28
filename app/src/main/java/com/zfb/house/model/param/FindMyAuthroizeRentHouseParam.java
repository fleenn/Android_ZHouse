package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

@Module(server = "zfb_server", name = "houseResource/v2")
public class FindMyAuthroizeRentHouseParam extends BaseParam {
    private int pageNo;
    private int pageSize;
    private String token;

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

    @Override
    public String toString() {
        return "FindMyAuthroizeRentHouseParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", token='" + token + '\'' +
                '}';
    }
}
