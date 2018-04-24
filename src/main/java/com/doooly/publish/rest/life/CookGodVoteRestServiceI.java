package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 *宝钢化工厨神大赛投票活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月6日
 */
public interface CookGodVoteRestServiceI {
	
	// 查询页面数据详情
	String getIndexData(JSONObject json);
	// 投票
	String vote(JSONObject json);
	// 投票排名详情
	String rank(JSONObject json);
	// 活动详情
	String activityDetail(JSONObject json);
}
