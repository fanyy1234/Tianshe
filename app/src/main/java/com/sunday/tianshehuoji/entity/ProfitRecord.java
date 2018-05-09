package com.sunday.tianshehuoji.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2016/12/28.
 */

public class ProfitRecord {


    /**
     * money : 6000
     * type : 2
     * fromName : 赵波
     * time : 2016-12-26 11:46:17
     */

    private BigDecimal money;
    private String type;
    private String fromName;
    private String time;
    private Integer recLevel;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getRecLevel() {
        return recLevel;
    }

    public void setRecLevel(Integer recLevel) {
        this.recLevel = recLevel;
    }
}
