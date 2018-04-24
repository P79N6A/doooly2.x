package com.doooly.entity.reachad;

/**
 * @className: AdAppUserCoupon
 * @description: 兜礼会员，券信息
 * @author: wangchenyu
 * @date: 2018-02-23 17:50
 */
public class AdAppUserCoupon {
	private long couponId;
	private long expiredDays;

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public long getExpiredDays() {
		return expiredDays;
	}

	public void setExpiredDays(long expiredDays) {
		this.expiredDays = expiredDays;
	}
}
