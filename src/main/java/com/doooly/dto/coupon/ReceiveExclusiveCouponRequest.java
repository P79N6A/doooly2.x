package com.doooly.dto.coupon;

import com.alibaba.fastjson.JSONObject;

/**
 * @className: ReceiveExclusiveCouponRequest
 * @description: 请求《专属优惠券》的request对象
 * @author: wangchenyu
 * @date: 2018-02-28 15:24
 */
public class ReceiveExclusiveCouponRequest {
	private Integer userId;
	private Integer activityId;
	private String couponIds;

	public ReceiveExclusiveCouponRequest() {
	}

	public ReceiveExclusiveCouponRequest(JSONObject json) {
		this.userId = json.getInteger("userId");
		this.activityId = json.getInteger("activityId");
		this.couponIds = json.getString("couponIds");
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(String couponIds) {
		this.couponIds = couponIds;
	}
}
