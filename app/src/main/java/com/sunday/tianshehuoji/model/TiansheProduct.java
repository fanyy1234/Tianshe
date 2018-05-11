package com.sunday.tianshehuoji.model;


import com.sunday.tianshehuoji.type.TypeFactory;

/**
 * Created by fanyy on 2018/4/2.
 */

public class TiansheProduct implements Visitable{
    private Integer id;
    private String img;
    private String name;
    private String newPrice;
    private String oldPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
