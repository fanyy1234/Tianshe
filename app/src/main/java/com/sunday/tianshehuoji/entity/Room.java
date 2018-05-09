package com.sunday.tianshehuoji.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2017/1/4.
 */

public class Room implements Serializable{


    /**
     * ct : 0
     * ut : 1483523987676
     * deleted : false
     * id : 1
     * shopId : 1
     * roomName :
     * image : https://day-mobile.tiansheguoji.com/uploadfiles/2017/1/f9a18666-be45-4ee7-9da8-aba659669654.jpg
     * detail :
     */

    private long ct;
    private long ut;
    private boolean deleted;
    private String id;
    private String shopId;
    private String roomName;
    private String image;
    private String detail;

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }

    public long getUt() {
        return ut;
    }

    public void setUt(long ut) {
        this.ut = ut;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
