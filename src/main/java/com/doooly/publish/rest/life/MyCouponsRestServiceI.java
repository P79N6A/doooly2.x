package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 我的卡券Service
 * 
 * @author yuelou.zhang
 * @version 2017年3月2日
 */
public interface MyCouponsRestServiceI {

	/**
	 * 获取卡券列表
	 * 
	 */
	String getCouponList(JSONObject obj);
}
