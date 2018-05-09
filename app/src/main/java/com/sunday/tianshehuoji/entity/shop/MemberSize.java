package com.sunday.tianshehuoji.entity.shop;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2017/4/21.
 */

public class MemberSize implements Serializable {

    private int id;
    private int memberId;
    private String name;
    private String yichang;
    private String jiankuan;
    private String xiongwei;
    private String yaowei;
    private String tunwei;
    private String xiuchang;
    private String remark;
    private boolean deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYichang() {
        return yichang;
    }

    public void setYichang(String yichang) {
        this.yichang = yichang;
    }

    public String getJiankuan() {
        return jiankuan;
    }

    public void setJiankuan(String jiankuan) {
        this.jiankuan = jiankuan;
    }

    public String getXiongwei() {
        return xiongwei;
    }

    public void setXiongwei(String xiongwei) {
        this.xiongwei = xiongwei;
    }

    public String getYaowei() {
        return yaowei;
    }

    public void setYaowei(String yaowei) {
        this.yaowei = yaowei;
    }

    public String getTunwei() {
        return tunwei;
    }

    public void setTunwei(String tunwei) {
        this.tunwei = tunwei;
    }

    public String getXiuchang() {
        return xiuchang;
    }

    public void setXiuchang(String xiuchang) {
        this.xiuchang = xiuchang;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
