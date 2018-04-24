package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 答题活动Service
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
public interface AppServiceI {

	/**
	 * 专享特权数据获取活动和商品list
	 * 
	 */
	String benefitsData(JSONObject obj);
}
