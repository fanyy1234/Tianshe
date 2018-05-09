package com.sunday.tianshehuoji.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/28.
 */

public class CashDetail {
    private int pageNo;
    private int pageSize;
    private BigDecimal total;
    private List<CashRecord> data;


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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<CashRecord> getData() {
        return data;
    }

    public void setData(List<CashRecord> data) {
        this.data = data;
    }
}
