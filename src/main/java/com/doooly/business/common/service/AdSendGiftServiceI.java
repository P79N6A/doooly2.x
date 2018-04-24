package com.doooly.business.common.service;

import com.doooly.dto.activity.ActivityOrderRes;
import com.doooly.entity.reachad.AdBusinessActivityOrder;

/**
 * @author lxl
 * @name 兑换券Service
 */
public interface AdSendGiftServiceI {
	
	/**
	 * @name 通过验证,送优惠码、发送短信,记录活动订单
	 * @param 商家订单信息
	 * @return infoMap(code、msg)
	 * */
	public ActivityOrderRes sendGift(AdBusinessActivityOrder adBusinessActivityOrder);
}
