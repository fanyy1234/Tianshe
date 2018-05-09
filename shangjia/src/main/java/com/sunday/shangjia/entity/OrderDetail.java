package com.sunday.shangjia.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2017/1/6.
 */

public class OrderDetail {


    /**
     * orderNo : 0367201482731383380
     * shopType : 2
     * arriveTime : null
     * leaveTime : 2016-12-30
     * linkMobile : 12222222222
     * linkName : 1333333333
     * desc : Aagagagaggagag
     * twocodeUrl : https://day-mobile-https.itboys.cc/uploadfiles/twocode/0367201482731383380.jpg
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
     * shopName : 宾馆
     * shopAddress : null
     * shopMobile : null
     * status : 1
     * cancelNo : 5810482316
     * product : [{"id":null,"name":"浪漫主题房","typeName":null,"num":1,"price":15,"logo":"https://day-mobile-https.itboys.cc/uploadfiles/2016/12/aa0d5bf3-6983-4c93-9f73-eece60ac526d.png","haveDetail":0,"images":null,"detail":null,"chooseService":0,"chooseWaiter":0}]
     */

    private String orderNo;
    private String shopType;
    private String arriveTime;
    private String leaveTime;
    private String linkMobile;
    private String linkName;
    private String desc;
    private String twocodeUrl;
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
    private String shopName;
    private String shopAddress;
    private String shopMobile;
    private int status;
    private String cancelNo;
    private List<SellerShopProductListBean> product;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopMobile() {
        return shopMobile;
    }

    public void setShopMobile(String shopMobile) {
        this.shopMobile = shopMobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCancelNo() {
        return cancelNo;
    }

    public void setCancelNo(String cancelNo) {
        this.cancelNo = cancelNo;
    }

    public List<SellerShopProductListBean> getProduct() {
        return product;
    }

    public void setProduct(List<SellerShopProductListBean> product) {
        this.product = product;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
}
