package com.doooly.business.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.service.UserDeliveryService;
import com.doooly.dao.reachad.AdUserDeliveryDao;
import com.doooly.entity.reachad.AdUserDelivery;

/**
 * 用户收货地址Service实现
 *
 * @author yuelou.zhang
 * @version 2017年9月27日
 */
@Service
@Transactional
public class UserDeliveryServiceImpl implements UserDeliveryService {

	private static Logger logger = Logger.getLogger(UserDeliveryServiceImpl.class);

	@Autowired
	private AdUserDeliveryDao adUserDeliveryDao;

	@Override
	public HashMap<String, Object> getUserDeliveryList(String userId) {
		logger.info("获取收货地址列表 userId=" + userId);
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 获取全部收货地址
			List<AdUserDelivery> userDeliveryList = adUserDeliveryDao.getUserDeliveryList(userId);
			// 将默认地址放在首位
			List<AdUserDelivery> newDeliveryList = new ArrayList<AdUserDelivery>();
			if (CollectionUtils.isNotEmpty(userDeliveryList)) {
				for (AdUserDelivery userDelivery : userDeliveryList) {
					if (AdUserDelivery.IS_DEFAULT.equals(userDelivery.getIsDefault())) {
						newDeliveryList.add(0, userDelivery);
						continue;
					}
					newDeliveryList.add(userDelivery);
				}
			}
			map.put("userDeliveryList", newDeliveryList);
		} catch (Exception e) {
			logger.error("获取收货地址列表异常！！！", e);
		}
		return map;
	}

	@Override
	public Integer insertUserDelivery(JSONObject obj) {
		logger.info("新增收货地址 obj=" + obj.toJSONString());
		int result = 0;
		try {
			// userId|姓名|电话|收货地址
			AdUserDelivery adUserDelivery = JSONObject.toJavaObject(obj, AdUserDelivery.class);
			// 获取用户已有的收货地址列表
			List<AdUserDelivery> userDeliveryList = adUserDeliveryDao.getUserDeliveryList(adUserDelivery.getUserId());
			if (CollectionUtils.isEmpty(userDeliveryList)) {
				// 设置 默认
				adUserDelivery.setIsDefault(AdUserDelivery.IS_DEFAULT);
			} else {
				// 设置 非默认
				adUserDelivery.setIsDefault(AdUserDelivery.NOT_DEFAULT);
			}
			adUserDelivery.setCreateDate(new Date());
			adUserDelivery.setUpdateDate(new Date());
			result = adUserDeliveryDao.insert(adUserDelivery);
		} catch (Exception e) {
			logger.error("新增收货地址异常", e);
		}
		return result;
	}

	@Override
	public Integer updateUserDelivery(JSONObject obj) {
		logger.info("更新收货地址 obj=" + obj.toJSONString());
		int result = 0;
		try {
			AdUserDelivery adUserDelivery = JSONObject.toJavaObject(obj, AdUserDelivery.class);
			adUserDelivery.setUpdateDate(new Date());
			result = adUserDeliveryDao.update(adUserDelivery);
		} catch (Exception e) {
			logger.error("更新收货地址异常", e);
		}
		return result;
	}

	@Override
	public Integer deleteUserDelivery(String id) {
		logger.info("删除收货地址 deliveryId=" + id);
		int result = 0;
		try {
			AdUserDelivery adUserDelivery = adUserDeliveryDao.get(id);
			if(adUserDelivery == null){
				result = 2;
			}else{
				result = adUserDeliveryDao.delete(adUserDelivery);
			}
		} catch (Exception e) {
			logger.error("删除收货地址异常", e);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getUserDeliveryById(String id) {
		logger.info("获取收货地址 deliveryId=" + id);
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 根据id获取收货地址
			AdUserDelivery userDelivery = adUserDeliveryDao.get(id);
			map.put("userDelivery", userDelivery != null ? userDelivery : new AdUserDelivery());
		} catch (Exception e) {
			logger.error("获取收货地址异常！！！", e);
		}
		return map;
	}

	@Override
	public Integer setDefaultDeliveryById(String userId, String deliveryId) {
		logger.info(String.format("设置默认收货地址 userId=%s , deliveryId=%s", userId, deliveryId));
		int result = 0;
		try {
			// 将用户当前默认地址改为非默认
			adUserDeliveryDao.updateNonDefaultDelivery(userId);
			// 更新某个地址为默认地址
			result = adUserDeliveryDao.updateDefaultDelivery(deliveryId);
		} catch (Exception e) {
			logger.error("设置默认收货地址异常！！！", e);
		}
		return result;
	}

}
