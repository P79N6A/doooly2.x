package com.doooly.entity.reachad;

import java.util.Date;

/**
 * Entity - 卡券报名记录
 * 
 * @version 1.0
 */
public class AdRegisterRecord {

	private int id;

	private int userId;// 领券用户ID

	private int activityId;// 券所属活动ID

	private int couponId;// 券ID

	private String couponCode;// 券码

	private String receiveStatus;// 券码发放后，用户领取状态（0-未获得券，1-获得券码）

	private Date createDate;// 报名时间

	private String businessId;// 商户编号

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

}