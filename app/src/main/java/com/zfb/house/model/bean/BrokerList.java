package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/6/30.
 */
public class BrokerList {

    /**
     * pageNo : 1
     * pageSize : 10
     * count : 24
     * firstResult : 0
     * maxResults : 10
     */

    private int pageNo;
    private int pageSize;
    private int count;
    private int firstResult;
    private int maxResults;
    private List<UserBean> list;

    public List<UserBean> getList() {
        return list;
    }

    public void setList(List<UserBean> list) {
        this.list = list;
    }

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
}
