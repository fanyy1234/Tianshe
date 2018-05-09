package com.sunday.tianshehuoji.model;


import com.sunday.tianshehuoji.type.TypeFactory;

/**
 * Created by fanyy on 2018/4/2.
 */

public class SubmitOrderProduct implements Visitable{
    private int num;
    private String name;
    private String guige;
    private String price;
    private String img;

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

    public String getGuige() {
        return guige;
    }

    public void setGuige(String guige) {
        this.guige = guige;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
