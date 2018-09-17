package com.doooly.business.report;

import com.doooly.entity.report.WechatEventPush;

public interface WechatEventPushService {
	/**
	 * 开发者模式微信推送事件信息存储
	 * 
	 * @author linking
	 * @date 创建时间：2018年4月17日
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	long insert(WechatEventPush wechatEventPush);
	
	Integer selectCountByEventKey(String eventKey);
}
