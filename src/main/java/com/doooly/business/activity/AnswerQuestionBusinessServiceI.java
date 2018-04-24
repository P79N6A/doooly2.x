package com.doooly.business.activity;

import java.util.HashMap;

/**
 * 答题活动业务Service
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
public interface AnswerQuestionBusinessServiceI {
	/**
	 * 获取题目及其选项
	 * 
	 * @param userId
	 *            会员id
	 * @param questionOrder
	 *            题目序号
	 * @param optionType
	 *            题目选项类型
	 */
	HashMap<String, Object> getTopicAndOptionsByUserIdAndOrder(String userId, String questionOrder, String optionType);

	/**
	 * 提交答案 && 获取心理年龄 && 发券
	 * 
	 * @param userId
	 *            会员id
	 * @param questionOrder
	 *            题目序号
	 * @param optionType
	 *            题目选项类型
	 */
	HashMap<String, Object> submitAnswer(String userId, String questionOrder, String optionType);

	/**
	 * 验证是否答题
	 * 
	 * @param userId
	 *            会员id
	 */
	HashMap<String, Object> validateAnswer(String userId);
}
