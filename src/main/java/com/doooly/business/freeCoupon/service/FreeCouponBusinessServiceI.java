package com.doooly.business.freeCoupon.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCouponCode;

public interface FreeCouponBusinessServiceI {

	MessageDataBean receiveCoupon(Integer memberId, Integer couponId, Integer activityId, String productSn);

	// int sendCoupon(Integer userId, String productSn);
	AdCouponCode sendCoupon(String businessId, Integer activityId, Integer userId, Integer couponId);
	/**
	 * 获取有效的卡券活动关联
	 * 
	 * @param userId
	 *            会员id
	 */
	HashMap<String, Object> findAvailableCoupons(String userId);

	/**
	 * 保存报名记录
	 * 
	 */
	HashMap<String, Object> saveRegisterRecord(JSONObject obj);

	/**
	 * 根据卡券活动关联id查询卡券详情
	 * 
	 * @param actConnId
	 *            卡券活动关联id
	 * @param userId
	 *            会员id
	 * @param userId
	 *            会员id
	 */
	HashMap<String, Object> getCouponDetailsByConnId(String actConnId, String userId);

	MessageDataBean forWuGangCouponSend(Integer adId, Integer couponId, Integer activityId, String productSn);

	List<String> sendCoupons(String businessId, Integer activityId,
			List<String> userIds, Integer couponId);

	void recyclingCoupon();

	MessageDataBean getIntegralActivityData(Long userId);

	MessageDataBean sendIntegralActivity(Long userId);
}
