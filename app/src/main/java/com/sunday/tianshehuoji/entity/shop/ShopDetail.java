package com.sunday.tianshehuoji.entity.shop;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/23.
 */

public class ShopDetail {

    /**
     * shopId : 3
     * imgList : ["http://day-admin.itboys.cc/uploadfiles/2016/12/f631127c-928f-4dcc-8f50-3f5c30f400b0.png","http://day-admin.itboys.cc/uploadfiles/2016/12/58dedfe3-06d0-4b64-a48e-dad70d5b6956.png"]
     * detail : 宾馆<span>宾馆</span><span>宾馆</span><span>宾馆</span><span>宾馆</span><span>宾馆</span><span>宾馆</span><span>宾馆</span><span>宾馆</span><span>宾馆</span><span>宾馆</span>
     * mobile : null
     * address : null
     * type : null
     * shopName : 宾馆
     * product : [{"typeName":"房间类型","sellerShopProductList":[{"id":3,"name":"浪漫主题房","typeName":null,"num":null,"price":15,"logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/aa0d5bf3-6983-4c93-9f73-eece60ac526d.png","haveDetail":1,"images":["http://day-admin.itboys.cc/uploadfiles/2016/12/72384503-50ef-42af-ae28-63ed73c35792.png"],"detail":"<p>\r\n\t<img src=\"/uploadfiles/2016/12/45c46314-7dea-4905-afac-62317389392d.png\" alt=\"\" />\r\n<\/p>\r\n<p>\r\n\t121212121212121212\r\n<\/p>","chooseService":0,"chooseWaiter":0},{"id":4,"name":"地中海风情房","typeName":null,"num":null,"price":20,"logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/4334ad33-6990-411a-849f-eab83ad58eac.png","haveDetail":1,"images":["http://day-admin.itboys.cc/uploadfiles/2016/12/420df660-81cd-4641-bd55-da4ccdc7c6a6.png"],"detail":"<p>\r\n\t<img src=\"/uploadfiles/2016/12/45c46314-7dea-4905-afac-62317389392d.png\" alt=\"\" />\r\n<\/p>\r\n<p>\r\n\t121212121212121212\r\n<\/p>","chooseService":0,"chooseWaiter":0},{"id":5,"name":"商务房","typeName":null,"num":null,"price":30,"logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/4fe61c98-b8d7-4cd9-b67f-ab1e243fb289.png","haveDetail":1,"images":["http://day-admin.itboys.cc/uploadfiles/2016/12/a4e0bf85-1fca-41f3-b226-027eb3a223e1.png"],"detail":"<p>\r\n\t<img src=\"/uploadfiles/2016/12/45c46314-7dea-4905-afac-62317389392d.png\" alt=\"\" /> \r\n<\/p>\r\n<p>\r\n\t12121212121212121212\r\n<\/p>","chooseService":0,"chooseWaiter":0}]}]
     */

    private String shopId;
    private String detail;
    private String mobile;
    private String address;
    private String type;
    private String shopName;
    private List<String> imgList;
    private List<ShopProduct> product;




    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }






    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ShopProduct> getProduct() {
        return product;
    }

    public void setProduct(List<ShopProduct> product) {
        this.product = product;
    }
}
