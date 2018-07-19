/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.entity.reachad.AdOrderReport;
import com.doooly.entity.reachad.AdUserBusinessExpansion;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AdOrderReportDao {
	/**
	 * 购买活动商品数量
	 */
	public int getByNum(@Param("userId") long userId, @Param("skuId") String skuId, @Param("actType") String actType);

	public BigDecimal getConsumptionAmount(@Param("userId") long userId);


	public int insert(OrderVo order);

	public int update(OrderVo order);
	public int updateByNum(OrderVo order);
	/**
	 * 取消订单
	 */
	public int cancleOrder(OrderVo order);
	/**
	 * 查询订单
	 */
	public List<OrderVo> getByOrderNum(String orderNum);
	public List<OrderVo> getOrder(OrderVo order);
	public OrderVo getById(String orderId);
	/**
	 * 获得用户某种sku的订单
	 */
	public List<Map<String,Object>> getByUserSku(@Param("userId") String userId,@Param("productSku") String productSku);

	public String findUserIsBuyByProductAndSkuId(@Param("userId")String userId, @Param("productSkuId")String productSkuId, @Param("productSku")String productSku);

    int getTotalNum(AdOrderReport adOrderReport);

    List<AdOrderReport> findOrderList(AdOrderReport adOrderReport);

    AdOrderReport getDetailByOrderReportId(long id);

    List<Map> findOrderSum(AdOrderReport adOrderReport);

    List<Map> findOrderSumByMonth(AdOrderReport adOrderReport);

    AdOrderReport getOrderDetailInfoById(String orderReportId);

    /**
     * 查询都市旅游卡，账户信息
     */
    AdUserBusinessExpansion findSctcdAccount(AdOrderReport adOrderReport);
}