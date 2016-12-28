package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/5/8.
 */
public class MomentsData {
    private int pageNo;
    private int pageSize;
    private int count;
    private List<MomentsContent> list;
    private int firstResult;
    private int maxResults;

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

    public List<MomentsContent> getList() {
        return list;
    }

    public void setList(List<MomentsContent> list) {
        this.list = list;
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
