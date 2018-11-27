package com.doooly.business.pay.service;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.dto.common.PayMsg;

public interface RefundService {
	
	
	//I、申请中，S、退款成功，F、退款失败;
	public final static String REFUND_STATUS_I = "i";
	public final static String REFUND_STATUS_S = "s";
	public final static String REFUND_STATUS_F = "f";
	
	public final static short PAY_TYPE_PLATFORM_POINT = (short)0;
	public final static short PAY_TYPE_WECHAT_APP = (short)3;
	public final static short PAY_TYPE_WECHAT_JSAPI = (short)10;
	
	public PayMsg autoRefund(long userId,String orderNum);//就支付自动退款
	
	public PayMsg refund(long userId,String orderNum);
	
	public PayMsg refund(OrderVo order,PayFlow payFlow);

	public ResultModel applyRefund(long userId, String orderNum, String totalAmount);

	public ResultModel dooolyCashDeskRefund(long userId, String orderNum, String returnFlowNumber, String payType);//兜礼收银台退款

}
