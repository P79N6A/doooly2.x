package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdActivityGift;

public interface AdActivityGiftDao extends BaseDaoI<AdActivityGift>{
	
	/**
	 * @name 查询商家对应活动礼品信息
	 * @param businessId-商家编号
	 * @return 礼品ID,礼品编号,礼品份数
	 * */
	public AdActivityGift getBusinessActivityGift(AdActivityGift adActivityGift);
	
}