package com.sunday.shangjia.entity;

/**
 * Created by 刘涛 on 2016/12/29.
 * 门店评分
 */


public class CommentScore {

    /**
     * health : 5
     * service : 4
     * quality : 4
     */

    private int health;
    private int service;
    private int quality;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
