package com.doooly.business.pay.processor.productprocessor;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.dto.common.PayMsg;

/***
 * 支付成功后, 后调用此接口的实现<br>
 * processCode和订单prodcutType
 * 
 * @author 2017-10-11 20:34:47 WANG
 *
 */
public interface ProductProcessor {
	
	//欧飞返回状态
	public static final String OF_SUCCESS_CODE = "1";
	//欧飞充值状态
	public static final String RECHARGE_STATE_0 = "0";
	public static final String RECHARGE_STATE_1 = "1";
	public static final String RECHARGE_STATE_9 = "9";
	
	//参考表: ad_self_product_type
//	public static final int PHYSICAL_CODE = 1; //威尔士卡
//	public static final int VIRTUAL_CODE = 2;  //虚拟卡密
//	public static final int MOBILE_CODE = 3;   //手机充值
//	public static final int FLOW_CODE = 4;	  //流量充值
	
	public int getProcessCode();
	
	public PayMsg process(OrderVo order);
	
}
