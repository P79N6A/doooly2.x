package com.doooly.business.pay.processor.afterpayprocessor;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.dto.common.PayMsg;

import java.util.Map;

/***
 * 支付成功后, 后调用此接口
 * 
 * @author 2017-10-11 20:34:47 WANG
 *
 */
public interface AfterPayProcessor {
	
	
	public PayMsg process(OrderVo order, Map<String, Object> payFlow, String realPayType);
	
}
