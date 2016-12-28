package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Administrator on 2016/6/21.k
 *
 */
@Module(server = "zfb_server", name = "houseElite/v2", httpMethod = "post")
public class CollectMomentsParam extends BaseParam {
    private Integer pageNo;
    private Integer pageSize;
    private String token;
    private String include;

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

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }
}
