package com.sunday.shangjia.entity;

import java.util.List;

import static u.aly.av.B;

/**
 * Created by 刘涛 on 2016/12/28.
 */

public class OrderTotal {

private int pageNo;
    private int pageSize;
    private Long total;
    private List<Order> data;

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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
