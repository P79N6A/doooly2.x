/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 兜礼自营电子卡券Entity
 * @author qing.zhang
 * @version 2018-04-08
 */
public class AdSelfProductCoupon {
	
	private String id;
	private String orderId;		// 使用订单号
	private String number;		// 批次商品编号
	private String couponCardNumber;		// 电子券卡号
	private String couponCardPassword;		// 电子券卡号
	private Integer purchaseStatus;		// 购买状态(1,未购买;2,已购买)
	private Integer useStatus;		// 使用状态(1,未使用;2,已使用)
	private String userCardNumber;		// 会员卡号
	private String telephone;		// 手机号
	private Date useDate;		// 使用时间
	private String createUser;		// 创建人
	private String updateUser;		// 修改人
    private Date buyStartDate;		// 查询开始时间
    private Date buyEndDate;		// 查询结束时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCouponCardNumber() {
        return couponCardNumber;
    }

    public void setCouponCardNumber(String couponCardNumber) {
        this.couponCardNumber = couponCardNumber;
    }

    public String getCouponCardPassword() {
        return couponCardPassword;
    }

    public void setCouponCardPassword(String couponCardPassword) {
        this.couponCardPassword = couponCardPassword;
    }

    public Integer getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(Integer purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public String getUserCardNumber() {
        return userCardNumber;
    }

    public void setUserCardNumber(String userCardNumber) {
        this.userCardNumber = userCardNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getBuyStartDate() {
        return buyStartDate;
    }

    public void setBuyStartDate(Date buyStartDate) {
        this.buyStartDate = buyStartDate;
    }

    public Date getBuyEndDate() {
        return buyEndDate;
    }

    public void setBuyEndDate(Date buyEndDate) {
        this.buyEndDate = buyEndDate;
    }

    @Override
    public String toString() {
        return "AdSelfProductCoupon{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", number='" + number + '\'' +
                ", couponCardNumber='" + couponCardNumber + '\'' +
                ", couponCardPassword='" + couponCardPassword + '\'' +
                ", purchaseStatus=" + purchaseStatus +
                ", useStatus=" + useStatus +
                ", userCardNumber='" + userCardNumber + '\'' +
                ", telephone='" + telephone + '\'' +
                ", useDate=" + useDate +
                ", createUser='" + createUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", buyStartDate=" + buyStartDate +
                ", buyEndDate=" + buyEndDate +
                '}';
    }
}