package com.doooly.business.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.PayMsg;

/**
 * 统一支付接口
 * 
 * @author 2017-10-21 14:45:22 WANG
 */
public interface PaymentService {
	
	public String getPayType() ;
	
	public PayMsg prePay(JSONObject json) ;

	public PayMsg handlePayResult(String retStr) ;
	
	public PayMsg getPayResult(String orderNum) ;
	
	

}
