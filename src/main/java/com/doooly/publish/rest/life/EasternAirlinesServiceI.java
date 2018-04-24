package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

public interface EasternAirlinesServiceI {

	String easternAirlines(JSONObject obj);
	
	String easternAirlinesCouponData(JSONObject obj);
	
	String receiveCoupon(JSONObject obj);
}
