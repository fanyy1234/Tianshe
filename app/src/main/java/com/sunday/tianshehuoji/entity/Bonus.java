package com.sunday.tianshehuoji.entity;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.List;

/**
 * Created by 刘涛 on 2017/1/9.
 * 分红
 */

public class Bonus {

    private BigDecimal bonusMoney;
    private BigDecimal withdrawMoney;
    private List<BonusRecord> list;

    public BigDecimal getBonusMoney() {
        return bonusMoney;
    }

    public void setBonusMoney(BigDecimal bonusMoney) {
        this.bonusMoney = bonusMoney;
    }

    public BigDecimal getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(BigDecimal withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public List<BonusRecord> getList() {
        return list;
    }

    public void setList(List<BonusRecord> list) {
        this.list = list;
    }
}
