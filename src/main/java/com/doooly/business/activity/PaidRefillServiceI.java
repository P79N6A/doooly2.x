package com.doooly.business.activity;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 *
 * @author yangwenwei
 * @date 2018年4月16日
 * @version 1.0
 */
public interface PaidRefillServiceI {

	MessageDataBean getPaidRefill(JSONObject json);

	MessageDataBean getIsHadDone(String userId);

	MessageDataBean getQRCode(String openId, String channel, String activityId);

    
}
