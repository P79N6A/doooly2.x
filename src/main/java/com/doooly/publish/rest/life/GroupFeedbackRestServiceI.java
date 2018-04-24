package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 反馈给公司Service
 * 
 * @author yuelou.zhang
 * @version 2017年3月6日
 */
public interface GroupFeedbackRestServiceI {

	/**
	 * 保存反馈内容
	 * 
	 */
	String saveFeedback(JSONObject obj);
}
