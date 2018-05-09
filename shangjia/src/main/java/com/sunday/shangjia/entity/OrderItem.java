package com.sunday.shangjia.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2016/12/28.
 */

public class OrderItem  {

    /**
     * productId : 1
     * num : 3
     * name : 蓝山咖啡
     * typeName : 咖啡
     * logo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/c04fa20a-7cb7-4b6b-8de5-204b46f72b16.png
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
