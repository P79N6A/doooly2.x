package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 *宝钢化工厨神大赛报名活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月4日
 */
public interface DoubleElevenActivityRestServiceI {
	
	// 活动页详情
	String activitIndex(JSONObject json);
	// 分享页详情
	String indexForJoiner(JSONObject json);
	// 点击帮他加分
	String help(JSONObject json);
	// 送清凉活动领取饮料
	String receiveGift(JSONObject json);
	// 判断是否领取过饮料
	String isReceiveGift(JSONObject json);
}
