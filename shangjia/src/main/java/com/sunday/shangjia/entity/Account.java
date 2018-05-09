package com.sunday.shangjia.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class Account implements Serializable {


    /**
     * id : 1
     * sellerChooseShopId : 23
     * shopName : 美甲
     * logo : http://admin.sunday.com/2016/12/bac74635-8c5a-4bae-bcc7-3da93a998980.jpg
     * userName : test
     * mobile : null
     */

    private String id;//账户Id;
    private String sellerChooseShopId;//shopId
    private String shopName;
    private String logo;
    private String userName;
    private String mobile;
    private String sellerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSellerChooseShopId() {
        return sellerChooseShopId;
    }

    public void setSellerChooseShopId(String sellerChooseShopId) {
        this.sellerChooseShopId = sellerChooseShopId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }


}
