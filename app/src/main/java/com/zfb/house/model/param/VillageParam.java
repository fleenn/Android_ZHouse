package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Administrator on 2016/5/13.
 */
@Module(server = "zfb_server", name = "common/v2")
public class VillageParam extends BaseParam {
    private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
