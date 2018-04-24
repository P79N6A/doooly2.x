package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.business.order.vo.OrderExtVo;

public interface AdOrderDeliveryDao {
	
	public int insert(@Param("orderId")long orderId,@Param("ext")OrderExtVo ext);
}