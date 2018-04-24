package com.doooly.business.activity;

import java.util.HashMap;

import com.doooly.dto.common.MessageDataBean;

/**
 * 答题活动业务Service
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
public interface BringCoolnessBusinessServiceI {
	/**
	 * 获取题目及其选项
	 * @param type 
	 * 
	 * @param userId
	 *            会员id
	 * @param questionOrder
	 *            题目序号
	 * @param optionType
	 *            题目选项类型
	 */

	public MessageDataBean sendCoolnessGift(Integer adId,Integer activityId, Integer type);

	public MessageDataBean getBringCoolnessUsers(Integer activityId);

}
