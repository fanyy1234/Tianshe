package com.sunday.tianshehuoji.entity.order;

import com.sunday.tianshehuoji.entity.ProfitRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2017/1/5.
 */

public class OrderTotal {

    private int pageNo;
    private int pageSize;
    private int total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
