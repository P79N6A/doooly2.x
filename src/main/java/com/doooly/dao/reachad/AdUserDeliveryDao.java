/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdUserDelivery;

/**
 * 用户收货地址DAO
 *
 * @author yuelou.zhang
 * @version 2017年9月27日
 */
public interface AdUserDeliveryDao extends BaseDaoI<AdUserDelivery> {

	/** 根据用户id获取收货地址列表 */
	List<AdUserDelivery> getUserDeliveryList(String userId);

	/** 获取用户默认收货地址 (如无默认地址则随机取一条) */
	AdUserDelivery getDefaultDelivery(String userId);

	/** 将用户默认地址更新为非默认 */
	Integer updateNonDefaultDelivery(String userId);

	/** 更新某个地址为默认地址 */
	Integer updateDefaultDelivery(String deliveryId);

}