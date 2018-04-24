package com.doooly.entity.coupon;

/**
 * @className: ReceiveExclusiveCoupon
 * @description: 领到的专属优惠券响应对象
 * @author: wangchenyu
 * @date: 2018-02-28 15:12
 */
public class ReceiveExclusiveCoupon {
	/** 用户id **/
	private Integer userId;
	/** 优惠券id **/
	private Integer couponId;
	/** 优惠券活动id **/
	private Integer activityId;
	/** 是否已领取优惠券，0-未领取，1-已领取 **/
	private Integer isReceived;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(Integer isReceived) {
		this.isReceived = isReceived;
	}
}
