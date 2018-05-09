package com.sunday.tianshehuoji.entity.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class Order {


    /**
     * orderNo : 0367201482731383380
     * memberId : 23
     * status : 1
     * shopId : null
     * shopName : 宾馆
     * shopType : 2
     * payMethod : null
     * createTime : 2016-12-26 13:49:43
     * updateTime : null
     * finishedTime : null
     * payTime : 2016-12-26 13:49:43
     * cancelTime : null
     * cancelReason : null
     * totalMoney : 15
     * discountPrice : 5
     * realMoney : 10
     * chooseServiceName : null
     * chooseWaiterName : null
     * chooseWaiterId : null
     * chooseServiceId : null
     * arriveTime : null
     * leaveTime : 2016-12-30
     * linkMobile : 12222222222
     * linkName : 1333333333
     * productType : null
     * desc : Aagagagaggagag
     * twocodeUrl : https://day-mobile-https.itboys.cc/uploadfiles/twocode/0367201482731383380.jpg
     * cancelNo : 5810482316
     * orderIteam : [{"productId":30,"num":1,"name":"浪漫主题房","typeName":"房间类型","logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/aa0d5bf3-6983-4c93-9f73-eece60ac526d.png","price":15,"chooseService":0,"chooseWaiter":0}]
     */

    private String orderNo;
    private String memberId;
    private int status;
    private String shopId;
    private String shopName;
    private int shopType;
    private String payMethod;
    private String createTime;
    private String updateTime;
    private String finishedTime;
    private String payTime;
    private String cancelTime;
    private String cancelReason;
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
    private String twocodeUrl;
    private String cancelNo;
    private List<OrderItem> orderIteam;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }



    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }




    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }






    public Object getChooseServiceName() {
        return chooseServiceName;
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


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTwocodeUrl() {
        return twocodeUrl;
    }

    public void setTwocodeUrl(String twocodeUrl) {
        this.twocodeUrl = twocodeUrl;
    }

    public String getCancelNo() {
        return cancelNo;
    }

    public void setCancelNo(String cancelNo) {
        this.cancelNo = cancelNo;
    }


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(BigDecimal realMoney) {
        this.realMoney = realMoney;
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

    public List<OrderItem> getOrderIteam() {
        return orderIteam;
    }

    public void setOrderIteam(List<OrderItem> orderIteam) {
        this.orderIteam = orderIteam;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }
}
