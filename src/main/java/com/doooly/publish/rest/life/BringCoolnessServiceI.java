package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 答题活动Service
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
public interface BringCoolnessServiceI {

	/**
	 * 送清凉发券
	 * 
	 */
	String bringCoolness(JSONObject obj);
	
	String coolUsers(JSONObject obj);
}
