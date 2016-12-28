package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 小区搜索
 * Created by Administrator on 2016/5/6.
 */
@Module(server = "zfb_server", name = "common/v2")
public class SearchPlotParam extends BaseParam {
    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    private String villageName;
}
