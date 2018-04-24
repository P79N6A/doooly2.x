package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

public interface WeiPinHuiRestServiceI {

	String getInfo(JSONObject obj);
	
	String easternAirlinesCouponData(JSONObject obj);
	
	String receiveCoupon(JSONObject obj);

	String receive(JSONObject obj);
}
