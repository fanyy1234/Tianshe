package com.sunday.shangjia.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2016/12/29.
 */

public class OrderComment {

    /**
     * id : 9
     * memberId : 33
     * mobile : 18610690003
     * logo : null
     * content : test
     * images : null
     * average : 5
     * commentTime : 2016-12-21 16:52:02
     */

    private String id;
    private String memberId;
    private String mobile;
    private String logo;
    private String content;
    private List<String> images;
    private int average;
    private String commentTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
