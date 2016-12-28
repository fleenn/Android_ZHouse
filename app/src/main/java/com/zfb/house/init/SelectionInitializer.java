package com.zfb.house.init;

import com.lemon.LemonCacheManager;
import com.lemon.LemonContext;
import com.lemon.init.AbstractInitializer;
import com.lemon.model.StatusCode;
import com.lemon.net.ApiManager;
import com.lemon.util.EventUtil;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.param.ReleasePullDataParam;
import com.zfb.house.model.result.ReleasePullDataResult;

/**
 * Created by Snekey on 2016/7/15.
 */
public class SelectionInitializer extends AbstractInitializer {

    @Override
    public Object initialize(Object... objects) throws Exception {
        EventUtil.register(this);
        ReleasePullDataParam param = new ReleasePullDataParam();
        LemonContext.getBean(ApiManager.class).getAll(param);
        return null;
    }

    public void onEventMainThread(ReleasePullDataResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            ReleasePull releasePull = result.getData();
            LemonContext.getBean(LemonCacheManager.class).putBean(ReleasePull.class, releasePull);
            EventUtil.unregister(this);
        }
    }
}
