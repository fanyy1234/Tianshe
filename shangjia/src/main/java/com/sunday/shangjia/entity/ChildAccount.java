package com.sunday.shangjia.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2016/12/30.
 */

public class ChildAccount implements Serializable{

    /**
     * id : 4
     * name : tssj007
     * type : 2
     */

    private String id;
    private String name;
    private int type;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
