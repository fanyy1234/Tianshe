package com.sunday.tianshehuoji.entity.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/23.
 */

public class SellerShopProductListBean implements Serializable{

    /**
     * id : 3
     * name : 浪漫主题房
     * typeName : null
     * num : null
     * price : 15
     * logo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/aa0d5bf3-6983-4c93-9f73-eece60ac526d.png
     * haveDetail : 1
     * images : ["http://day-admin.itboys.cc/uploadfiles/2016/12/72384503-50ef-42af-ae28-63ed73c35792.png"]
     * detail : <p>
     <img src="/uploadfiles/2016/12/45c46314-7dea-4905-afac-62317389392d.png" alt="" />
     </p>
     <p>
     121212121212121212
     </p>
     * chooseService : 0
     * chooseWaiter : 0
     */

    private String id;
    private String name;
    private String typeName;
    private Integer num;
    private BigDecimal price;
    private String logo;
    private int haveDetail;
    private String detail;
    private int chooseService;
    private int chooseWaiter;
    private List<String> images;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getHaveDetail() {
        return haveDetail;
    }

    public void setHaveDetail(int haveDetail) {
        this.haveDetail = haveDetail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getChooseService() {
        return chooseService;
    }

    public void setChooseService(int chooseService) {
        this.chooseService = chooseService;
    }

    public int getChooseWaiter() {
        return chooseWaiter;
    }

    public void setChooseWaiter(int chooseWaiter) {
        this.chooseWaiter = chooseWaiter;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


}
