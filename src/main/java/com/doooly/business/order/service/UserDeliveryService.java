package com.doooly.business.order.service;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;

public interface UserDeliveryService {

	/**
	 * 获取用户收货地址列表
	 * 
	 * @param userId
	 *            会员id
	 */
	HashMap<String, Object> getUserDeliveryList(String userId);

	/** 新增收货地址 */
	Integer insertUserDelivery(JSONObject obj);

	/** 更新收货地址 */
	Integer updateUserDelivery(JSONObject obj);

	/** 删除收货地址 */
	Integer deleteUserDelivery(String id);

	/** 获取收货地址 */
	HashMap<String, Object> getUserDeliveryById(String id);

	/** 设置默认收货地址 */
	Integer setDefaultDeliveryById(String userId, String deliveryId);
}
