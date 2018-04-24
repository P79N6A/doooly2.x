package com.doooly.business.common.service;

import com.doooly.entity.reachad.AdActivityGift;

/**
 * @author lxl
 * @name 活动礼品Service
 */
public interface AdActivityGiftServiceI{
	
	/**
	 * 查询商家对应活动礼品信息
	 * @param businessId-商家编号
	 * @return 礼品ID,礼品编号,礼品份数
	 */
	public AdActivityGift getBusinessActivityGift(AdActivityGift adActivityGift);
}
