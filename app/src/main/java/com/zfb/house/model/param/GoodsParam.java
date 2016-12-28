package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Administrator on 2016/8/8.
 */
@Module(server = "zfb_server",name = "mall/v2")
public class GoodsParam extends BaseParam {
    private String itemId;//商品ID

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
