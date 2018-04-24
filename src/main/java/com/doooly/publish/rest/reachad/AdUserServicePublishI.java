package com.doooly.publish.rest.reachad;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;

public interface AdUserServicePublishI {
	/**
	 * 批量发送短信
	 * 
	 * @param paraJSON
	 * @return
	 */
	JSONObject batchSendSms(JSONObject paramJSON);

	/**
	 * B库用户验证(如：登录)
	 * 
	 * @param JSONObject
	 * @return JSONObject
	 */
	JSONObject validateUserInfo(JSONObject param);

	/**
	 * 用户有卡激活
	 * 
	 * @param JSONObject
	 * @return JSONObject
	 */
	JSONObject validateAndActive(JSONObject param);

	/**
	 * 企业口令激活
	 * 
	 * @param
	 * @return JSONObject groupQuestion-企业口令问题
	 */
	String getGroupCommandInfo(JSONObject param);

	/**
	 * 企业口令获取企业集合
	 * 
	 * @param groupCommand-企业口令
	 * @return groupList-企业集合
	 */
	String getGroupByCommand(JSONObject param);

	/**
	 * 企业口令激活
	 * 
	 * @param
	 * @return 用户信息
	 */
	String groupCommandActive(JSONObject param);

	BaseRes<Object> checkQuickLogin(JSONObject req);

	BaseRes<JSONObject> batchUpdateUser(JSONObject req);
	// /**
	// * 定时对比昨天的东航员工数据
	// * */
	// void scheduleUpdateUser(JSONObject req);
	// /**
	// * 初始化对比东航员工数据
	// * */
	// void initializeSchedulecheduleUpdateUser(JSONObject req);

}
