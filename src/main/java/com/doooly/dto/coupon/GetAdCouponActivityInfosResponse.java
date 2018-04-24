package com.doooly.dto.coupon;

import com.doooly.dto.base.BaseResponse;
import com.doooly.entity.coupon.AdCouponActivityInfos;

/**
 * @className: GetAdCouponActivityInfosResponse
 * @description: 优惠券活动信息，封装的响应对象
 * @author: wangchenyu
 * @date: 2018-02-27 19:22
 */
public class GetAdCouponActivityInfosResponse extends BaseResponse {
	private AdCouponActivityInfos data;

	public AdCouponActivityInfos getData() {
		return data;
	}

	public void setData(AdCouponActivityInfos data) {
		this.data = data;
	}
}
