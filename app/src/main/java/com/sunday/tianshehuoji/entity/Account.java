package com.sunday.tianshehuoji.entity;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2016/12/13.
 */

public class Account implements Serializable {
    /**
     * id : 23
     * realName : null
     * mobile : 18710010492
     * logo : null
     * dLevel : 首批会员
     * proportion : 0.4%
     * energyBean : 3980000
     * nowTotalMoney : 1660000
     */


    private Long id;
    private String realName;
    private String mobile;
    private String logo;
    private String dLevel;
    private String proportion;
    private BigDecimal energyBean;//消费积分
    private BigDecimal nowTotalMoney;//可提现

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getDLevel() {
        return dLevel;
    }

    public void setDLevel(String dLevel) {
        this.dLevel = dLevel;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }



    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BigDecimal getEnergyBean() {
        return energyBean;
    }

    public void setEnergyBean(BigDecimal energyBean) {
        this.energyBean = energyBean;
    }

    public BigDecimal getNowTotalMoney() {
        return nowTotalMoney;
    }

    public void setNowTotalMoney(BigDecimal nowTotalMoney) {
        this.nowTotalMoney = nowTotalMoney;
    }
}
