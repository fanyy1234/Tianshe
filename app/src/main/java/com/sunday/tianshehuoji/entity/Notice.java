package com.sunday.tianshehuoji.entity;

/**
 * Created by 刘涛 on 2016/12/23.
 */

public class Notice {


    /**
     * id : 16
     * type : 2
     * title : 开业庆典
     * image : http://day-shop.itboys.cc/uploadfiles/2016/12/69ff42e6-73d5-40bc-9bb8-c4eb89d24fc2.png
     * content : 恭喜杭州市西湖天奢国际生活馆隆重开业
     * url : null
     */

    private String id;
    private int type;
    private String title;
    private String image;
    private String content;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
