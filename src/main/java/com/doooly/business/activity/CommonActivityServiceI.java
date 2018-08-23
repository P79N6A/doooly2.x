package com.doooly.business.activity;

import org.json.JSONObject;

import com.doooly.dto.common.MessageDataBean;

/**
 * 
    * @ClassName: MURechargeActivityI  
    * @Description: 东航话费充值活动
    * @author hutao  
    * @date 2018年8月23日  
    *
 */
public interface CommonActivityServiceI {

	MessageDataBean sendActivityCoupon(Integer userId, Integer activityId);
}
