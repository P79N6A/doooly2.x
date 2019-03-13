package com.doooly.entity.meituan;


import com.doooly.common.meituan.OrderStatusEnum;

import java.math.BigDecimal;

/**
 * Created by wanghai on 2018/12/19.
 */
public class Order {

    private int entId;

    //原价
    private BigDecimal originAmount;

    //支付金额
    private BigDecimal payAmount;

    //退款金额
    private BigDecimal refundAmount;

    //业务类型
    private int bizType;

    //支付时间
    private long payTime;

    //订单号
    private String orderSN;

    //类型 10 企业支付；20 员工垫付
    private int payMode;

    //订单状态
    private int orderStatus;

    //商品名称
    private String goodsName;

    //员工工号
    private String staffSN;

    //店铺id
    private String shopID;

    //店铺名称
    private String shopName;

    //店铺电话
    private String shopPhone1;

    //店铺电话
    private String shopPhone2;

    //展示图片
    private String shopDefaultPic;

    //店铺地址
    private String address;

    //ZZYES外卖专用
    private int lateststatus;

    //ZZYES外卖专用
    private String updateTime;

    //ZZYES外卖专用
    private String priceInfo;

    //ZZYES外卖专用
    private String orderInfo;



    public int getEntId() {
        return entId;
    }

    public void setEntId(int entId) {
        this.entId = entId;
    }

    public BigDecimal getOriginAmount() {
        return originAmount;
    }

    public void setOriginAmount(BigDecimal originAmount) {
        this.originAmount = originAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public int getPayMode() {
        return payMode;
    }

    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getStaffSN() {
        return staffSN;
    }

    public void setStaffSN(String staffSN) {
        this.staffSN = staffSN;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopPhone1() {
        return shopPhone1;
    }

    public void setShopPhone1(String shopPhone1) {
        this.shopPhone1 = shopPhone1;
    }

    public String getShopPhone2() {
        return shopPhone2;
    }

    public void setShopPhone2(String shopPhone2) {
        this.shopPhone2 = shopPhone2;
    }

    public String getShopDefaultPic() {
        return shopDefaultPic;
    }

    public void setShopDefaultPic(String shopDefaultPic) {
        this.shopDefaultPic = shopDefaultPic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLateststatus() {
        return lateststatus;
    }

    public void setLateststatus(int lateststatus) {
        this.lateststatus = lateststatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(String priceInfo) {
        this.priceInfo = priceInfo;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
}
