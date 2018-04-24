package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 卡券发放失败记录表-表名：ad_give_coupon_error
 * 
 * @version 1.0
 */
public class AdGiveCouponError {

	private int id;
	private int activityId;// 标题
	private int couponId;// 图片路径
	private int userId;// 广告显示类别，默认为0(0-微信端，1-PC端)
	private Date createDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}