package com.sunday.tianshehuoji.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class CartItem implements Serializable {


    /**
     * ct : 1492594551136
     * ut : 1492595832508
     * id : 1
     * memberId : 1
     * userId : 0
     * type : 1
     * productId : 3
     * productName : 美迪惠尔可莱丝韩国针剂水库面膜男女贴补水保湿美肤白皙收缩毛孔
     * price : 1
     * totalPrice : 1
     * num : 1
     * elements : nullnull
     * paramId : 14
     * image : http://static.zj-yunti.com/upload/2017/4/4dff64c2-87dd-4004-97d7-a5af76043667.jpg
     * limitBuyId : null
     * productStore : 10
     * scale : 0
     */

    private int id;
    private int cartId;
    private int productId;
    private int num;
    private String name;
    private String typeName;
    private String logo;
    private int price;
    private int chooseService;
    private int chooseWaiter;
    private int categoryId;


    private boolean isSelect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
