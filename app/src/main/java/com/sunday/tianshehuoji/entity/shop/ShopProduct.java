package com.sunday.tianshehuoji.entity.shop;

import java.util.List;

/**
 * Created by 刘涛 on 2016/12/23.
 */

public class ShopProduct {
    /**
     * typeName : 房间类型
     * sellerShopProductList : [{"id":3,"name":"浪漫主题房","typeName":null,"num":null,"price":15,"logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/aa0d5bf3-6983-4c93-9f73-eece60ac526d.png","haveDetail":1,"images":["http://day-admin.itboys.cc/uploadfiles/2016/12/72384503-50ef-42af-ae28-63ed73c35792.png"],"detail":"<p>\r\n\t<img src=\"/uploadfiles/2016/12/45c46314-7dea-4905-afac-62317389392d.png\" alt=\"\" />\r\n<\/p>\r\n<p>\r\n\t121212121212121212\r\n<\/p>","chooseService":0,"chooseWaiter":0},{"id":4,"name":"地中海风情房","typeName":null,"num":null,"price":20,"logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/4334ad33-6990-411a-849f-eab83ad58eac.png","haveDetail":1,"images":["http://day-admin.itboys.cc/uploadfiles/2016/12/420df660-81cd-4641-bd55-da4ccdc7c6a6.png"],"detail":"<p>\r\n\t<img src=\"/uploadfiles/2016/12/45c46314-7dea-4905-afac-62317389392d.png\" alt=\"\" />\r\n<\/p>\r\n<p>\r\n\t121212121212121212\r\n<\/p>","chooseService":0,"chooseWaiter":0},{"id":5,"name":"商务房","typeName":null,"num":null,"price":30,"logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/4fe61c98-b8d7-4cd9-b67f-ab1e243fb289.png","haveDetail":1,"images":["http://day-admin.itboys.cc/uploadfiles/2016/12/a4e0bf85-1fca-41f3-b226-027eb3a223e1.png"],"detail":"<p>\r\n\t<img src=\"/uploadfiles/2016/12/45c46314-7dea-4905-afac-62317389392d.png\" alt=\"\" /> \r\n<\/p>\r\n<p>\r\n\t12121212121212121212\r\n<\/p>","chooseService":0,"chooseWaiter":0}]
     */

    private String typeName;
    private List<SellerShopProductListBean> sellerShopProductList;

    private boolean isChecked;


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<SellerShopProductListBean> getSellerShopProductList() {
        return sellerShopProductList;
    }

    public void setSellerShopProductList(List<SellerShopProductListBean> sellerShopProductList) {
        this.sellerShopProductList = sellerShopProductList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
