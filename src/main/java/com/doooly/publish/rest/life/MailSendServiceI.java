package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信商城获取数据
 * 
 * @author 杨汶蔚
 * @date 2017年2月9日
 * @version 1.0
 */
public interface MailSendServiceI {
	/** 机票退票发送邮件 */
	public void sendMailForRefundTicket(JSONObject json);

	
}
