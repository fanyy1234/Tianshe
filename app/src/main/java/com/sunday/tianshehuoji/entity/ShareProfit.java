package com.sunday.tianshehuoji.entity;

import java.math.BigDecimal;
import java.util.List;

import static android.R.id.list;

/**
 * Created by 刘涛 on 2017/1/11.
 */

public class ShareProfit {
    private BigDecimal totalMoney;
    private BigDecimal totalWithdraw;
    private Profit list;

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(BigDecimal totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }


    public Profit getList() {
        return list;
    }

    public void setList(Profit list) {
        this.list = list;
    }
}
