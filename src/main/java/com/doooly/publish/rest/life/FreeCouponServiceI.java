package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseReq;

/**
 * 微信商城获取数据
 * 
 * @author 杨汶蔚
 * @date 2017年2月9日
 * @version 1.0
 */
public interface FreeCouponServiceI {

	public String receiveCoupon(JSONObject json);
	public String forWuGangCouponSend(JSONObject json);
	public String grabCoupons(JSONObject obj);
	public String saveRegisterRecord(JSONObject obj) ;
	public String couponDetail(JSONObject obj) ;
	public String recyclingCoupon(JSONObject obj);
	public String integralActivity(BaseReq<JSONObject> json);
	public String sendIntegralActivity(BaseReq<JSONObject> json);

	public String setUserPrivilege(JSONObject json);
	public String getUserPrivilege(JSONObject json) ;
	}
