package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户收货地址 REST Service
 * 
 * @author yuelou.zhang
 * @version 2017年9月27日
 */
public interface UserDeliveryRestServiceI {

	/** 获取收货地址列表 */
	String getUserDeliveryList(JSONObject obj);

	/** 新增收货地址 */
	String insertUserDelivery(JSONObject obj);

	/** 更新收货地址 */
	String updateUserDelivery(JSONObject obj);

	/** 删除收货地址 */
	String deleteUserDelivery(JSONObject obj);
	
	/** 获取收货地址 */
	String getUserDelivery(JSONObject obj);
	
	/** 设置默认收货地址 */
	String setDefaultDelivery(JSONObject obj);

}
