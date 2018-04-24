package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 答题活动Service
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
public interface AnswerQuestionRestServiceI {
	/**
	 * 获取题目及其选项
	 * 
	 */
	String showQuestion(JSONObject obj);
	
	/**
	 * 提交答案
	 * 
	 */
	String submitAnswer(JSONObject obj);
	
	/**
	 * 开始答题时验证是否答题
	 * 
	 */
	String validateAnswer(JSONObject obj);
}
