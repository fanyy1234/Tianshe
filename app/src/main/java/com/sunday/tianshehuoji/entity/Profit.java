package com.sunday.tianshehuoji.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/28.
 */

public class Profit {


    /**
     * pageNo : 1
     * pageSize : 20
     * total : 1
     * data : [{"money":6000,"type":"2","fromName":"赵波","time":"2016-12-26 11:46:17"}]
     */

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
