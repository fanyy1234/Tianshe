package com.sunday.tianshehuoji.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2016/12/28.
 */

public class CashRecord {

    /**
     * type : 1
     * time : 2016-12-26 16:06:05
     * withdrawsMoney : 21.55
     * bankName : 中国农业银行
     */

    private int type;
    private String time;
    private BigDecimal withdrawsMoney;
    private String bankName;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getWithdrawsMoney() {
        return withdrawsMoney;
    }

    public void setWithdrawsMoney(BigDecimal withdrawsMoney) {
        this.withdrawsMoney = withdrawsMoney;
    }
}
