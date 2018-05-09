package com.sunday.tianshehuoji.entity.order;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2016/12/27.
 */

public class OrderItem {


    /**
     * productId : 30
     * num : 1
     * name : 浪漫主题房
     * typeName : 房间类型
     * logo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/aa0d5bf3-6983-4c93-9f73-eece60ac526d.png
     * price : 15
     * chooseService : 0
     * chooseWaiter : 0
     */

    private String productId;
    private int num;
    private String name;
    private String typeName;
    private String logo;
    private BigDecimal price;
    private int chooseService;
    private int chooseWaiter;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



    public int getChooseService() {
        return chooseService;
    }

    public void setChooseService(int chooseService) {
        this.chooseService = chooseService;
    }

    public int getChooseWaiter() {
        return chooseWaiter;
    }

    public void setChooseWaiter(int chooseWaiter) {
        this.chooseWaiter = chooseWaiter;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
