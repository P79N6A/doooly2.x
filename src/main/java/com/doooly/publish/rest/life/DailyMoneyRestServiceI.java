package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 每日赚钱Service
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
public interface DailyMoneyRestServiceI {

	/**
	 * 获取用户答题情况(包括昨日收入、总财富值等)
	 * 
	 */
	String getAnswerSituation(JSONObject obj);

	/**
	 * 获取题目及其选项
	 * 
	 */
	String showQuestion(JSONObject obj);

	/**
	 * 回答问题&&提交答案
	 * 
	 */
	String answerQuestion(JSONObject obj);

	/**
	 * 每日赚钱结算
	 * 
	 */
	void dailyMoneyScheduled(JSONObject obj);
}
