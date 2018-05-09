package com.sunday.shangjia.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/29.
 */

public class Profit {
    private int pageNo;
    private int pageSize;
    private BigDecimal total;
    private List<ProfitRecord> data;

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

    public List<ProfitRecord> getData() {
        return data;
    }

    public void setData(List<ProfitRecord> data) {
        this.data = data;
    }
}
