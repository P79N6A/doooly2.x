package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;

public interface EasternAirlinesBusinessServiceI {

	MessageDataBean eastAirlinesEfamily(Integer userId, String activityId);

	MessageDataBean eastAirlinesEfamilyCouponCheck(String activityId);

	MessageDataBean receiveCoupon(Integer userId, String activityId,String couponId);

}
