package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.OrderCps;

/**
 * _order_cps订单营销费用表DAO
 * 
 * @author tangzhiyuan
 *
 */
public interface OrderCpsDao extends BaseDaoI<OrderCps> {
	/**
	 * 获取订单营销费用详情
	 * 
	 * @param orderNumber
	 * @return
	 */
	OrderCps getByOrderNumber(@Param("userId") Long userId, @Param("businessId") Long businessId,
			@Param("orderNumber") String orderNumber);

	/**
	 * 获取用户享受过几笔订单优惠
	 * 
	 * @param userId
	 * @param businessId
	 * @return
	 */
	Integer getMaxCpsNumber(@Param("userId") Long userId, @Param("businessId") Long businessId);
}
