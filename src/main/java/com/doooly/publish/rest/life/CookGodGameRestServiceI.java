package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 *宝钢化工厨神大赛报名活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月4日
 */
public interface CookGodGameRestServiceI {
	
	// 查询活动详情
	String getActivityDetail(JSONObject json);
	// 活动报名详情
	String apply(JSONObject json);
}
