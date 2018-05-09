package com.sunday.tianshehuoji.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2017/1/3.
 */

public class Vote {

    private int pageNo;
    private int pageSize;
    private int total;
    private List<VoteItem> data;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<VoteItem> getData() {
        return data;
    }

    public void setData(List<VoteItem> data) {
        this.data = data;
    }
}
