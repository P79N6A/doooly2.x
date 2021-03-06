package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseReq;

/**
 * @Description: 我的积分
 * @author: qing.zhang
 * @date: 2017-05-18
 */
public interface MyPointRestServiceI {

	// 查询可用积分和共享积分
	String queryUserIntegral(JSONObject obj);

	// 获得某个用户的所有可用积分列表信息
	String getAvailablePoints(JSONObject obj);

	// 获得某个用户的所有待返积分列表信息
	String getReturnPoints(JSONObject obj);

	// 获得某个用户的所有可用积分详细信息
	String getAvailablePointDetail(JSONObject obj);

	// 获得某个用户的所有待返积分详细信息
	String getReturnPointDetail(JSONObject obj);
	// 积分卡充值
	String integralRechargeDo(JSONObject json);
	// 积分卡充值时获取充值记录
	String integralRechargeList(JSONObject json);

}
