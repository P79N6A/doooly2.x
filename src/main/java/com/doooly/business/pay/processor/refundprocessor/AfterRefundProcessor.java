package com.doooly.business.pay.processor.refundprocessor;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.Order;

/***
 * 退款成功后调用此接口
 *
 * @author 2017-11-29 15:49:30 WANG
 *
 */
public interface AfterRefundProcessor {

	public PayMsg process(OrderVo order, Order o);
	
}
