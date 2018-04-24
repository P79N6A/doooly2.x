package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdBusinessActivityOrder;

public interface AdBusinessActivityOrderDao {

	public AdBusinessActivityOrder getActivityOrder(@Param("userId") Long userId,
			@Param("activityId") Integer activityId, @Param("businessId") String businessId);
	
	public AdBusinessActivityOrder getByOrderNumber(@Param("orderNumber") String orderNumber);

	/** 保存商家活动订单信息 */
	public void insert(AdBusinessActivityOrder adBusinessActivityOrder);
}