package com.sunday.shangjia.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class Order {


    /**
     * orderNo : 0372081482416394518
     * memberId : 23
     * status : 1
     * shopId : null
     * shopName : 咖啡厅
     * shopType : null
     * payMethod : null
     * createTime : 2016-12-22 22:19:54
     * updateTime : null
     * finishedTime : null
     * payTime : 2016-12-22 22:19:54
     * cancelTime : null
     * cancelReason : null
     * totalMoney : 81
     * discountPrice : 25
     * realMoney : 56
     * chooseServiceName : null
     * chooseWaiterName : null
     * chooseWaiterId : null
     * chooseServiceId : null
     * arriveTime : null
     * leaveTime : null
     * linkMobile : 13333333333
     * linkName : 阿发
     * productType : 1
     * desc : 1
     * twocodeUrl : https://day-mobile-https.itboys.cc/uploadfiles/twocode/0372081482416394518.jpg
     * cancelNo : null
     * orderIteam : [{"productId":1,"num":3,"name":"蓝山咖啡","typeName":"咖啡","logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/c04fa20a-7cb7-4b6b-8de5-204b46f72b16.png","price":15,"chooseService":0,"chooseWaiter":0},{"productId":2,"num":2,"name":"拿铁","typeName":"咖啡","logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/4151d541-f3aa-4829-9021-40293c707025.png","price":18,"chooseService":0,"chooseWaiter":0}]
     */

    private String orderNo;
    private String memberId;
    private Integer status;
    private String shopId;
    private String shopName;
    private String shopType;
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
    private String startTime;
    private String endTime;
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


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
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

    public List<OrderItem> getOrderIteam() {
        return orderIteam;
    }

    public void setOrderIteam(List<OrderItem> orderIteam) {
        this.orderIteam = orderIteam;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
