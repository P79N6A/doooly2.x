package com.doooly.business.dailymoney.service;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 * 每日赚钱业务Service
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
public interface DailyMoneyBusinessServiceI {

	/**
	 * 获取用户答题情况(包括昨日收入、总财富值等)
	 * 
	 * @param userId
	 *            会员id
	 */
	HashMap<String, Object> getAnswerSituation(String userId);

	/**
	 * 获取题目及其选项
	 * 
	 * @param userId
	 *            会员id
	 */
	HashMap<String, Object> getTopicAndOptions(String userId);

	/**
	 * 回答问题&&提交答案
	 * 
	 * @param obj
	 *            答题记录JSONObject
	 */
	Integer saveAnswerRecord(JSONObject obj);

	/**
	 * 更新用户财富值定时任务
	 * 
	 * 
	 */
	void updateUserWealth();

	MessageDataBean getUserCode(String telephone);
	
}
