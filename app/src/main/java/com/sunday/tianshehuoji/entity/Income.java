package com.sunday.tianshehuoji.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/1/9.
 * 新收益统计/分红和分享
 */

public class Income {

    private BigDecimal totalMoney;
    private BigDecimal totalWithdraw;

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
}
