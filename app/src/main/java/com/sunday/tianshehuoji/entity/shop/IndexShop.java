package com.sunday.tianshehuoji.entity.shop;

import com.sunday.tianshehuoji.entity.Img;
import com.sunday.tianshehuoji.entity.Notice;
import com.sunday.tianshehuoji.entity.shop.SellerShop;

import java.util.List;

/**
 * Created by 刘涛 on 2016/12/23.
 */

public class IndexShop {
    private String sellerId;
    private String sellerName;
    private List<SellerShop> sellerShopList;
    private List<Notice> noticeList;
    private List<Img> imgList;

    public List<SellerShop> getSellerShopList() {
        return sellerShopList;
    }

    public void setSellerShopList(List<SellerShop> sellerShopList) {
        this.sellerShopList = sellerShopList;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Notice> noticeList) {
        this.noticeList = noticeList;
    }

    public List<Img> getImgList() {
        return imgList;
    }

    public void setImgList(List<Img> imgList) {
        this.imgList = imgList;
    }
}
