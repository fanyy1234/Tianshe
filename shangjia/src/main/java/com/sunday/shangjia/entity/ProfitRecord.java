package com.sunday.shangjia.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2016/12/29.
 */

public class ProfitRecord {

    private BigDecimal money;
    private String orderNo;
    private String shopName;
    private String time;
    private String memberName;




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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
