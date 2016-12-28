package com.lemon.event;

/**
 * 项目名称:  [Lemon]
 * 包:        [com.lemon.event]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/1/31 15:51]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/1/31 15:51]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public class CloseEvent {
    String type;

    public CloseEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
