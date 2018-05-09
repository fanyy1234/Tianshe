package com.sunday.shangjia.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2016/12/29.
 */

public class ProfitTotal {

    /**
     * sellerId : null
     * shopId : 15
     * thisMonthTotalMoney : 2
     * otherMonthTotalMoney : 0
     * otherMonthTotalCostMoney : 0
     * otherMonthTotalProfitMoney : 0
     */

    private String sellerId;
    private String shopId;
    private BigDecimal thisMonthTotalMoney;
    private BigDecimal otherMonthTotalMoney;
    private BigDecimal otherMonthTotalCostMoney;
    private BigDecimal otherMonthTotalProfitMoney;


    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getThisMonthTotalMoney() {
        return thisMonthTotalMoney;
    }

    public void setThisMonthTotalMoney(BigDecimal thisMonthTotalMoney) {
        this.thisMonthTotalMoney = thisMonthTotalMoney;
    }

    public BigDecimal getOtherMonthTotalMoney() {
        return otherMonthTotalMoney;
    }

    public void setOtherMonthTotalMoney(BigDecimal otherMonthTotalMoney) {
        this.otherMonthTotalMoney = otherMonthTotalMoney;
    }

    public BigDecimal getOtherMonthTotalCostMoney() {
        return otherMonthTotalCostMoney;
    }

    public void setOtherMonthTotalCostMoney(BigDecimal otherMonthTotalCostMoney) {
        this.otherMonthTotalCostMoney = otherMonthTotalCostMoney;
    }

    public BigDecimal getOtherMonthTotalProfitMoney() {
        return otherMonthTotalProfitMoney;
    }

    public void setOtherMonthTotalProfitMoney(BigDecimal otherMonthTotalProfitMoney) {
        this.otherMonthTotalProfitMoney = otherMonthTotalProfitMoney;
    }
}
