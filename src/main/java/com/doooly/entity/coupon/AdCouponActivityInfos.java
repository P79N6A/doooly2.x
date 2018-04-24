package com.doooly.entity.coupon;

/**
 * @className: AdCouponActivityInfos
 * @description: 优惠券活动表，封装的信息对象
 * @author: wangchenyu
 * @date: 2018-03-29 19:36
 */
public class AdCouponActivityInfos {
	/** 活动名称 **/
	private String activityName;
	/** 活动类型 **/
	private String activityType;
	/** 活动介绍，可能会配置外部商户的跳转URL **/
	private String introduction;

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
}
