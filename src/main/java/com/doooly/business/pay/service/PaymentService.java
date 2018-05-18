package com.doooly.business.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.PayMsg;

/**
 * 统一支付接口
 * 
 * @author 2017-10-21 14:45:22 WANG
 */
public interface PaymentService {

	public static final String CHANNEL = "channel";
	public static final String CHANNEL_WECHAT = "wechat";
	public static final String CHANNEL_APP = "app";
	public static final String CHANNEL_H5 = "h5";
	public static final String CHANNEL_WISCOAPP = "wiscoapp";
	public static final String CHANNEL_WISCOWECHAT = "wiscowechat";
	
	public String getPayType() ;
	
	public PayMsg prePay(JSONObject json) ;

	public PayMsg handlePayResult(String retStr,String channel) ;
	
	public PayMsg getPayResult(String orderNum) ;
	
	

}
