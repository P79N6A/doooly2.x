package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * APP首页优惠券 REST Service
 * 
 * @author yuelou.zhang
 * @version 2017年8月11日
 */
public interface HomeCouponRestServiceI {

	/** 获取优惠券对应的商家列表 */
	String getBusinessList(JSONObject obj);

	/** 获取商家的优惠券列表 */
	String getCouponList(JSONObject obj);

}
