package com.doooly.business.freeCoupon.service;

import java.util.HashMap;

public interface MyCouponsBusinessServiceI {

	/**
	 * 根据会员id && 查询类型 获取卡券列表
	 * 
	 * @param userId
	 *            会员id
	 * @param couponType
	 *            查询类型
	 * @param couponCategory
	 *            卡券类型
	 */
	HashMap<String, Object> getCouponListByType(String userId, String couponType,String couponCategory);

	/**
	 * 根据会员id && 卡券活动关联id 获取卡券详细信息
	 * 
	 * @param userId
	 *            会员id
	 * @param actConnId
	 *            卡券活动关联id
	 */
	HashMap<String, Object> getCouponDetail(String userId, String actConnId);
	
	/**
	 * 获取可用抵扣券数量
	 * 
	 * @param userId
	 *            会员id
	 * @param amount
	 *            商品金额
	 */
	HashMap<String, Object> getVoucherCouponNum(String userId,String amount);
}
