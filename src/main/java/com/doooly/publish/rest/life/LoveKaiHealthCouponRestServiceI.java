package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 爱启健康优惠券活动 rest 接口
 * 
 * @author yuelou.zhang
 * @date 2017年9月18日
 * @version 1.0
 */
public interface LoveKaiHealthCouponRestServiceI {

	/**
	 * 爱启健康优惠券活动领券
	 * 
	 * @param obj
	 *            请求参数
	 */
	String receiveCoupon(JSONObject obj);

}
