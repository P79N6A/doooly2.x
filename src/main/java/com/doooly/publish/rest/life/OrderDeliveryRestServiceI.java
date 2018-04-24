package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 确认下单 REST Service
 * 
 * @author yuelou.zhang
 * @version 2017年9月27日
 */
public interface OrderDeliveryRestServiceI {

	/** 获取确认下单页数据 */
	String getOrderDeliveryInfo(JSONObject obj);

	/** 获取门店列表集合 */
	String getStoreList(JSONObject obj);

}
