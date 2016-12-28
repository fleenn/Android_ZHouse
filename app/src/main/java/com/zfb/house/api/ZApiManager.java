package com.zfb.house.api;

import android.content.Context;

import com.lemon.annotation.Autowired;
import com.lemon.annotation.Component;
import com.lemon.net.NetEngine;

/**
 * 项目名称:  [zfb_house]
 * 包:        [com.zfb.house.api]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/4/14 22:32]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/4/14 22:32]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Component
public class ZApiManager {

    @Autowired
    public Context mContext;
    @Autowired
    public NetEngine netEngine;//


}
