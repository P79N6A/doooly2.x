package com.doooly.business.order.service;

import java.util.HashMap;

public interface OrderDeliveryService {

	/**
	 * 获取确认下单页数据
	 * 
	 * @param userId
	 *            会员id
	 * @param productTypeId
	 *            商品类型主键
	 * @param deliveryId
	 *            选择的收货地址id
	 */
	HashMap<String, Object> getOrderDeliveryInfo(String userId, String productTypeId, String deliveryId);

	/**
	 * 获取确认下单页数据
	 * 
	 * @param businessId
	 *            商家id
	 * @param province
	 *            省
	 * @param city
	 *            市
	 * @param area
	 *            区
	 */
	HashMap<String, Object> getStoreList(String businessId, String province, String city, String area);

}
