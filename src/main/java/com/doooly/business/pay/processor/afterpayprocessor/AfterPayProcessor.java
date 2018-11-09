package com.doooly.business.pay.processor.afterpayprocessor;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.dto.common.PayMsg;

/***
 * 支付成功后, 后调用此接口
 * 
 * @author 2017-10-11 20:34:47 WANG
 *
 */
public interface AfterPayProcessor {
	
	
	public PayMsg process(OrderVo order, PayFlow payFlow, String realPayType);
	
}
