package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.business.order.vo.OrderItemVo;

public interface AdOrderDetailDao {
	
	public int insert(OrderItemVo item);
	
	public int update(OrderItemVo item);
	
	public int bantchInsert(@Param("orderId")long orderId,@Param("items")List<OrderItemVo> items);
}