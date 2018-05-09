package com.sunday.tianshehuoji.entity.shop;

/**
 * Created by 刘涛 on 2016/12/23.
 */

public class SellerShop {

    /**
     * id : 15
     * sellerId : 36
     * shopId : 2
     * show : 1
     * name : 咖啡厅
     * indexLogo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/2600a49b-3f5f-4c11-86b4-27a6e780da95.png
     * indexMinLogo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/92581dbb-f6db-417b-a73e-71fe8c78e5ea.png
     * type : 8
     */

    private String id;
    private String sellerId;
    private String shopId;
    private int show;
    private String name;
    private String indexLogo;
    private String indexMinLogo;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndexLogo() {
        return indexLogo;
    }

    public void setIndexLogo(String indexLogo) {
        this.indexLogo = indexLogo;
    }

    public String getIndexMinLogo() {
        return indexMinLogo;
    }

    public void setIndexMinLogo(String indexMinLogo) {
        this.indexMinLogo = indexMinLogo;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
