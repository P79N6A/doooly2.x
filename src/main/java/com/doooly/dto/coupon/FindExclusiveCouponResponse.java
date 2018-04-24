package com.doooly.dto.coupon;

import com.doooly.dto.base.BaseResponse;
import com.doooly.entity.coupon.ExclusiveCoupon;

import java.util.List;

/**
 * @className: FindExclusiveCouponResponse
 * @description: 查询到的专属优惠券响应对象
 * @author: wangchenyu
 * @date: 2018-02-27 19:22
 */
public class FindExclusiveCouponResponse extends BaseResponse {
	/** 专属优惠券是否全部领取，0-未全部领取，1-全部领取 **/
	private int isReceivedAll;
	/** 专属优惠券的信息集合 **/
	private List<ExclusiveCoupon> data;

	public int getIsReceivedAll() {
		return isReceivedAll;
	}

	public void setIsReceivedAll(int isReceivedAll) {
		this.isReceivedAll = isReceivedAll;
	}

	public void setIsReceivedAll(List<ExclusiveCoupon> data){
		this.isReceivedAll = 1; //先默认：全部领取专属优惠券
		if (data != null && data.size() > 0) {
			for (ExclusiveCoupon exclusiveCoupon : data) {
				if (exclusiveCoupon.getIsReceived() == 0) {
					this.isReceivedAll = 0;
					break;
				}
			}
		} else {
			this.isReceivedAll = 0;
		}
	}

	public List<ExclusiveCoupon> getData() {
		return data;
	}

	public void setData(List<ExclusiveCoupon> data) {
		this.data = data;
	}
}
