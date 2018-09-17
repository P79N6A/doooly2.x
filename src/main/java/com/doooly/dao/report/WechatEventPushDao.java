/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.report;

import com.doooly.entity.report.WechatEventPush;

/**
 * report库DAO接口
 * 
 * @author linking
 * @version 2017-04-17
 */
public interface WechatEventPushDao {

	/**
	 * 微信事件推送信息存储
	 */
	long insert(WechatEventPush wechatEventPush);
	
	Integer selectCountByEventKey(String eventKey);
}