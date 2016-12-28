package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/7/7.
 */
public class Relevant {

    /**
     * pageNo : 1
     * pageSize : 1
     * count : 49
     * list : [{"id":null,"replyUserName":"大神好ghhjjkjijjjhhyyy","replyUserPhoto":"http://7xid0d.com1.z0.glb.clouddn.com/43daf292-3e55-4a89-950e-b3d57d7609ddgeolo","replyUserId":"7b5cc4e4237d419f83a11418285c0ffc","replyContent":"鹭江","replyeeUserId":null,"replyeeUserName":null,"replyTime":"2016-07-07 15:05:17.0","eliteId":"0ee809f167eb4c31b243c101505957ea","eliteContent":"hhjj","elitePhoto":null,"userId":null}]
     * firstResult : 0
     * maxResults : 1
     */

    private int pageNo;
    private int pageSize;
    private int count;
    private int firstResult;
    private int maxResults;
    private List<RelevantContent> list;

    public List<RelevantContent> getList() {
        return list;
    }

    public void setList(List<RelevantContent> list) {
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
