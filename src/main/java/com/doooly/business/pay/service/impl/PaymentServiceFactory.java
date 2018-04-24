package com.doooly.business.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.doooly.business.pay.processor.productprocessor.ProductProcessor;
import com.doooly.business.pay.service.PaymentService;
import com.doooly.common.context.SpringContextHolder;

/**
 * 支付工厂类
 * 
 * 2017-10-21 16:14:00 WANG
 */
public class PaymentServiceFactory{

	private static Map<String, PaymentService> payServiceBeanMap;
	
	static{
		Map<String, PaymentService> map = SpringContextHolder.getBeansOfType(PaymentService.class);
		payServiceBeanMap = new HashMap<String, PaymentService>();
		for (Map.Entry<String, PaymentService> ele : map.entrySet()) {
			payServiceBeanMap.put(ele.getValue().getPayType(), ele.getValue());
		}
	}

	public static <T extends PaymentService> T getPayService(String payType) {
		return (T) payServiceBeanMap.get(payType);
	}

}
