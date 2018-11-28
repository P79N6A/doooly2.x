package com.doooly.dao.reachad;

import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.entity.reachad.AdOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdOrderDetailDao {
	
	public int insert(OrderItemVo item);
	
	public int update(OrderItemVo item);
	
	public int bantchInsert(@Param("orderId")long orderId,@Param("items")List<OrderItemVo> items);

    List<AdOrderDetail> findListByAdOrderReport(AdOrderDetail adOrderDetailQuery);

	List<AdOrderDetail> finDetailByOrder(List<Long> list);
}