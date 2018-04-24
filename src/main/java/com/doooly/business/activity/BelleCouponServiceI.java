package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;

/**
 * 百丽优惠券活动 business接口
 * 
 * @author yuelou.zhang
 * @date 2017年9月18日
 * @version 1.0
 */
public interface BelleCouponServiceI {

	MessageDataBean receiveCoupon(String userId, String activityId);

	public MessageDataBean receiveCoupon2(String userId, String flag) ;
}
