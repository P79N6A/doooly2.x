package com.doooly.business.report.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doooly.business.report.WechatEventPushService;
import com.doooly.dao.report.WechatEventPushDao;
import com.doooly.entity.report.WechatEventPush;

/**
 * 
 * @ClassName: WechatEventPushServiceImpl
 * @Description: 处理微信回调事件信息存储
 * @author linking
 * @date 2018年4月17日
 *
 */
@Service
public class WechatEventPushServiceImpl implements WechatEventPushService {
	// private static Logger logger =
	// LoggerFactory.getLogger(WechatEventPushServiceImpl.class);

	@Autowired
	private WechatEventPushDao wechatEventPushDao;

	/**
	 * 微信推送事件信息存储
	 * 
	 * @author linking
	 * @date 创建时间：2018年4月17日
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	public long insert(WechatEventPush wechatEventPush) {
		return wechatEventPushDao.insert(wechatEventPush);
	}
}
