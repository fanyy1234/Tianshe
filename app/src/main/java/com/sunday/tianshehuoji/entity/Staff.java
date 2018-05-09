package com.sunday.tianshehuoji.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2016/12/30.
 */

public class Staff implements Serializable{

    /**
     * id : 11
     * name : test
     * logo : null
     * mobile : 18988887777
     */

    private String id;
    private String name;
    private String logo;
    private String mobile;

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



    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
