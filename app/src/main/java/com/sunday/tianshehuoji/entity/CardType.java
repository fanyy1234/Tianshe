package com.sunday.tianshehuoji.entity;

/**
 * Created by 刘涛 on 2016/12/19.
 * 银行卡
 */

public class CardType {


    /**
     * ct : 1482482727836
     * ut : 1482482727836
     * deleted : false
     * id : 5
     * name : 兴业银行
     * logo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/451bd3bc-7e7a-45fc-b6c4-3c8a59ba7fe3.png
     * fullLogo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/2543f8ca-d08c-4ae3-ac6f-a192583382ec.gif
     */

    private long ct;
    private long ut;
    private boolean deleted;
    private String id;
    private String name;
    private String logo;
    private String fullLogo;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFullLogo() {
        return fullLogo;
    }

    public void setFullLogo(String fullLogo) {
        this.fullLogo = fullLogo;
    }
}
