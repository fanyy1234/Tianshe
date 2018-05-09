package com.sunday.tianshehuoji.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/1/9.
 */

public class BonusRecord implements Serializable {

    private String date;
    private BigDecimal money;
    private String start;
    private String end;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
