package com.doooly.business.freeCoupon.service;

import java.util.HashMap;

/**
 * APP首页优惠券 业务Service
 * 
 * @author yuelou.zhang
 * @version 2017年8月11日
 */
public interface HomeCouponServiceI {

	/**
	 * 获取优惠券对应的商家列表
	 * 
	 */
	HashMap<String, Object> getBusinessList();

	/**
	 * 获取商家的优惠券列表
	 * 
	 * @param businessId
	 *            商家主键
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * 
	 */
	HashMap<String, Object> getCouponListByBusinessId(String businessId,String categoryType, int currentPage, int pageSize);

}
