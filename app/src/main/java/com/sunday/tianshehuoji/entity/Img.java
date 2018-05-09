package com.sunday.tianshehuoji.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2016/12/23.
 */

public class Img implements Serializable {


    /**
     * id : 17
     * type : 1
     * title : null
     * image : http://day-shop.itboys.cc/uploadfiles/2016/12/096a5b1c-7a55-40ec-ab53-27e42e7aec16.png
     * content : null
     * url : https://day-mobile-https.itboys.cc/mobile/info
     */

    private String id;
    private String type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
