package com.doooly.business.order.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doooly.business.order.service.OrderDeliveryService;
import com.doooly.dao.reachad.AdBusinessStoreDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.AdUserDeliveryDao;
import com.doooly.entity.reachad.AdBusinessStore;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserDelivery;

import cn.jiguang.common.utils.StringUtils;

/**
 * 确认下单Service实现
 *
 * @author yuelou.zhang
 * @version 2017年9月27日
 */
@Service
@Transactional
public class OrderDeliveryServiceImpl implements OrderDeliveryService {

	private static Logger logger = Logger.getLogger(OrderDeliveryServiceImpl.class);
	// 卡券类型 实体卡id
	private static final String ENTITY_CARD_ID = "1";
	// 卡券类型 电子卡id
	private static final String VIRTUAL_CARD_ID = "2";

	@Autowired
	private AdUserDeliveryDao adUserDeliveryDao;
	@Autowired
	private AdBusinessStoreDao adBusinessStoreDao;
	@Autowired
	private AdUserDao adUserDao;

	@Override
	public HashMap<String, Object> getOrderDeliveryInfo(String userId, String productTypeId, String deliveryId) {
		logger.info(String.format("获取确认下单页数据 userId=%s , productTypeId=%s, deliveryId=%s", userId, productTypeId,
				deliveryId));
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			AdUserDelivery defaultDelivery = new AdUserDelivery();
			// 1.实体卡类型取默认地址
			if (ENTITY_CARD_ID.equals(productTypeId)) {
				if (StringUtils.isNotEmpty(deliveryId)) {
					// 选择地址后跳转确认下单页返回此地址
					defaultDelivery = adUserDeliveryDao.get(deliveryId);
				} else {
					// 获取用户默认收货地址 如无默认地址则随机取一条
					defaultDelivery = adUserDeliveryDao.getDefaultDelivery(userId);
				}
			}
			// 2.电子卡取当前用户姓名、电话
			if (VIRTUAL_CARD_ID.equals(productTypeId)) {
				AdUser user = adUserDao.getById(Integer.valueOf(userId));
				defaultDelivery.setReceiverName(user.getName());
				defaultDelivery.setReceiverTelephone(user.getTelephone());
			}
			map.put("defaultDelivery", defaultDelivery != null ? defaultDelivery : new AdUserDelivery());
		} catch (Exception e) {
			logger.error("获取确认下单页数据异常！！！", e);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getStoreList(String businessId, String province, String city, String area) {
		logger.info(String.format("获取门店列表 province=%s ,city=%s ,area=%s", province, city, area));
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 根据省市区获取对应的门店列表
			List<AdBusinessStore> storeList = adBusinessStoreDao.findStoreList(businessId, province, city, area);
			map.put("storeList", storeList != null ? storeList : new ArrayList<AdBusinessStore>());
		} catch (Exception e) {
			logger.error("获取门店列表集合异常！！！", e);
		}
		return map;
	}

}
