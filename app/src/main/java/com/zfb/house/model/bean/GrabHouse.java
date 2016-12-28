package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/6/18.
 */
public class GrabHouse {

    /**
     * pageNo : 1
     * pageSize : 10
     * count : 0
     * list : []
     * firstResult : 0
     * maxResults : 10
     */

    private int pageNo;
    private int pageSize;
    private int count;
    private int firstResult;
    private int maxResults;
    private List<GrabHouseItem> list;

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

    public List<GrabHouseItem> getList() {
        return list;
    }

    public void setList(List<GrabHouseItem> list) {
        this.list = list;
    }
}
