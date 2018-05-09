package com.sunday.tianshehuoji.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class CashAccount implements Serializable {


    /**
     * ct : 1482547791052
     * ut : 1482547791052
     * deleted : false
     * id : 7
     * memberId : 23
     * bank : 中国农业银行
     * account : 36589741582246524
     * accountName : 张三
     * bankLogo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/2ec95e5a-05b0-429a-bb1a-fb3ae272accf.png
     * bankId : 4
     */

    private long ct;
    private long ut;
    private boolean deleted;
    private String id;
    private String memberId;
    private String bank;
    private String account;
    private String accountName;
    private String bankLogo;
    private String bankId;

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }

    public long getUt() {
        return ut;
    }

    public void setUt(long ut) {
        this.ut = ut;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
}
