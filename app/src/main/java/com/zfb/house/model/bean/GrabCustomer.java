package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/6/16.
 */
public class GrabCustomer {
    List<GrabCustomerItem> data;

    public List<GrabCustomerItem> getData() {
        return data;
    }

    public void setData(List<GrabCustomerItem> data) {
        this.data = data;
    }
}
