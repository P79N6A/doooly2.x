package com.doooly.dto.coupon;

import com.doooly.dto.base.BaseResponse;
import com.doooly.entity.coupon.ReceiveExclusiveCoupon;

import java.util.List;

/**
 * @className: ReceiveExclusiveCouponResponse
 * @description: 领取《专属优惠券》响应对象
 * @author: wangchenyu
 * @date: 2018-02-28 15:10
 */
public class ReceiveExclusiveCouponResponse extends BaseResponse {
	/** 兜礼APP首页数据 **/
	private List<ReceiveExclusiveCoupon> data;

	public List<ReceiveExclusiveCoupon> getData() {
		return data;
	}

	public void setData(List<ReceiveExclusiveCoupon> data) {
		this.data = data;
	}
}
