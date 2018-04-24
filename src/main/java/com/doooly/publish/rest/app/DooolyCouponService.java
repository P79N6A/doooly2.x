package com.doooly.publish.rest.app;

import com.alibaba.fastjson.JSONObject;

import javax.ws.rs.core.Response;

/**
 * Created by 王晨宇 on 2018/2/27.
 */
public interface DooolyCouponService {
	/**
	 * 查询《专属优惠券》列表，同时根据 isReceived 字段区分是否领取过该券
	 */
	Response findExclusiveCoupon(String userId);
	/**
	 * 用户领取《专属优惠券》
	 */
	Response receiveExclusiveCoupon(JSONObject json);
	/**
	 * 在redis中设置专属优惠券活动id
	 */
	Response setExclusiveCouponActivityId(JSONObject json);
	/**
	 * 发现列表页，点击图片跳转后，获取优惠券活动表信息
	 */
	Response getAdCouponActivityInfos(String activityId);
}
