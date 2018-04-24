package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;


public interface YanXuanBusinessServiceI {

	MessageDataBean getCounpon(String userId, String telephone, String type,
			String cardNumber ,String activityId);

	MessageDataBean sendWangYiCoupon(String data);

	MessageDataBean sendCoupons(String data);


}
