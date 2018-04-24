package com.doooly.business.myaccount.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 反馈给公司业务Service
 * 
 * @author yuelou.zhang
 * @version 2017年3月6日
 */
public interface GroupFeedbackBusinessServiceI {

	/**
	 * 保存企业反馈
	 * 
	 * @param obj
	 *            企业反馈JSONObject
	 */
	Integer saveFeedback(JSONObject obj);

}
