package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

public interface YanXuanRestServiceI {

	String getCoupon(JSONObject obj);

	String sendWangYiCoupon(String obj);

	String getCouponInfo(JSONObject obj);

	String myCoupon(JSONObject obj);
	
	String getCouponTwoInfo(JSONObject obj);

	String getPrivilege(JSONObject obj);
	
	String toBind(JSONObject obj);
	
	String sendCoupons(String msg);
}
