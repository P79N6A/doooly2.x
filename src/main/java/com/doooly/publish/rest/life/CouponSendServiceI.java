package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信商城获取数据
 * 
 * @author 杨汶蔚
 * @date 2017年2月9日
 * @version 1.0
 */
public interface CouponSendServiceI {
	/** 0元抢券发券 */
	public void sendCoupon();

	/** 每日赚钱结算 */
	public void updateWealth();

	/**
	 * 回答问题&&提交答案
	 * 
	 */
	String getUserCode(JSONObject obj);

}
