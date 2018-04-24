package com.doooly.publish.rest.reachad;

import com.alibaba.fastjson.JSONObject;

public interface AdBusinessActivityServiceI {
	/**
	 * 活动规则验证通过,发放礼品、发送短信,存储订单记录
	 * @param 
	 * @return
	 */
	JSONObject senGift(String jsonStr);
}
