package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 */
public interface VipWealServiceI {
	/**
	 * 专享特权数据获取活动和商品list
	 * 
	 */
	String benefitsData(JSONObject obj);
	
	
	String getIntegralAndBusinessList(JSONObject obj);
}
