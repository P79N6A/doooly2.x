package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;

public interface WeiPinHuiBusinessServiceI {

	MessageDataBean getInfo(Integer userId, String activityId);

	MessageDataBean getCouponConInfo(String activityId);

	MessageDataBean receiveCoupon(Integer userId, String activityId,String couponId);

	MessageDataBean receive(Integer userId, String activityId, String couponId);

}
