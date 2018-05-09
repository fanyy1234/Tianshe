package com.sunday.tianshe.branch.entity;

/**
 * Created by admin on 2017/1/5.
 */

public class Shop {
    private Long shopId;
    private String shopName;
    private int otherMonthTotalMoney;
    private int otherMonthTotalProfitMoney;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getOtherMonthTotalMoney() {
        return otherMonthTotalMoney;
    }

    public void setOtherMonthTotalMoney(int otherMonthTotalMoney) {
        this.otherMonthTotalMoney = otherMonthTotalMoney;
    }

    public int getOtherMonthTotalProfitMoney() {
        return otherMonthTotalProfitMoney;
    }

    public void setOtherMonthTotalProfitMoney(int otherMonthTotalProfitMoney) {
        this.otherMonthTotalProfitMoney = otherMonthTotalProfitMoney;
    }
}
