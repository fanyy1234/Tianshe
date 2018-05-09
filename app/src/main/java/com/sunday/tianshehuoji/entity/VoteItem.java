package com.sunday.tianshehuoji.entity;

/**
 * Created by 刘涛 on 2017/1/3.
 */

public class VoteItem {


    /**
     * title : test
     * image : https://day-mobile-https.itboys.cc/uploadfileshttp://admin.sunday.com/2017/1/3c046cca-5cfa-455a-a1c1-bbd5f981a0ef.jpg
     * content : test
     * deadline : 2017-01-17 19:24:17
     * agree : 0
     * against : 0
     */
    private String id;
    private String title;
    private String image;
    private String content;
    private String deadline;
    private int agree;
    private int against;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getAgree() {
        return agree;
    }

    public void setAgree(int agree) {
        this.agree = agree;
    }

    public int getAgainst() {
        return against;
    }

    public void setAgainst(int against) {
        this.against = against;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
