package com.sunday.tianshehuoji.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/27.
 */

public class OrderConfirm implements Serializable{


    /**
     * ct : 1482822141955
     * ut : 1482822141955
     * deleted : false
     * id : 36
     * memberId : 23
     * shopId : 15
     * totalMoney : 18
     * discountPrice : 6
     * realMoney : 12
     * chooseServiceName : null
     * chooseWaiterName : null
     * chooseWaiterId : null
     * chooseServiceId : null
     * arriveTime : null
     * leaveTime : null
     * linkMobile : null
     * linkName : null
     * productType : null
     * desc : null
     * startTime : null
     * endTime : null
     * shopType : 8
     * iteamList : [{"ct":1482822141970,"ut":1482822141970,"deleted":false,"id":57,"cartId":36,"productId":29,"num":1,"name":"拿铁","typeName":"咖啡","logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/4151d541-f3aa-4829-9021-40293c707025.png","price":18,"chooseService":0,"chooseWaiter":0}]
     * canUseMoney : 39798
     */

    private long ct;
    private long ut;
    private boolean deleted;
    private String id;//购物车ID
    private String memberId;
    private String shopId;
    private BigDecimal totalMoney;
    private BigDecimal discountPrice;
    private BigDecimal realMoney;
    private String chooseServiceName;
    private String chooseWaiterName;
    private String chooseWaiterId;
    private String chooseServiceId;
    private String arriveTime;
    private String leaveTime;
    private String linkMobile;
    private String linkName;
    private String productType;
    private String desc;
    private String startTime;
    private String endTime;
    private String shopType;
    private BigDecimal canUseMoney;
    private List<IteamListBean> iteamList;

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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }



    public List<IteamListBean> getIteamList() {
        return iteamList;
    }

    public void setIteamList(List<IteamListBean> iteamList) {
        this.iteamList = iteamList;
    }

    public String getChooseServiceName() {
        return chooseServiceName;
    }

    public void setChooseServiceName(String chooseServiceName) {
        this.chooseServiceName = chooseServiceName;
    }

    public String getChooseWaiterName() {
        return chooseWaiterName;
    }

    public void setChooseWaiterName(String chooseWaiterName) {
        this.chooseWaiterName = chooseWaiterName;
    }

    public String getChooseWaiterId() {
        return chooseWaiterId;
    }

    public void setChooseWaiterId(String chooseWaiterId) {
        this.chooseWaiterId = chooseWaiterId;
    }

    public String getChooseServiceId() {
        return chooseServiceId;
    }

    public void setChooseServiceId(String chooseServiceId) {
        this.chooseServiceId = chooseServiceId;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }



    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(BigDecimal realMoney) {
        this.realMoney = realMoney;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public BigDecimal getCanUseMoney() {
        return canUseMoney;
    }

    public void setCanUseMoney(BigDecimal canUseMoney) {
        this.canUseMoney = canUseMoney;
    }

    public static class IteamListBean implements Serializable {
        /**
         * ct : 1482822141970
         * ut : 1482822141970
         * deleted : false
         * id : 57
         * cartId : 36
         * productId : 29
         * num : 1
         * name : 拿铁
         * typeName : 咖啡
         * logo : https://day-mobile-https.itboys.cc/uploadfiles/2016/12/4151d541-f3aa-4829-9021-40293c707025.png
         * price : 18
         * chooseService : 0
         * chooseWaiter : 0
         */

        private long ct;
        private long ut;
        private boolean deleted;
        private String id;
        private String cartId;
        private String productId;
        private int num;
        private String name;
        private String typeName;
        private String logo;
        private BigDecimal price;
        private int chooseService;
        private int chooseWaiter;

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

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
