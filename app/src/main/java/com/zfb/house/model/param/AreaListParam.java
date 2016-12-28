package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 获取省市区以及片区
 * Created by Administrator on 2016/5/13.
 */
@Module(server = "zfb_server", name = "common/v2")
public class AreaListParam extends BaseParam {
    private String level;
    private String parentId;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
