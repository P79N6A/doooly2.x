package com.doooly.business.pay.service;

import com.doooly.business.pay.bean.PayFlow;

public interface PayFlowService {
	/**
	 * 微信支付结果
	 */
	public static final String FAIL_MSG = "失败";
	public static final String FAIL_CODE = "-1";
	public static final String SUCCESS_MSG = "支付成功";
	public static final String SUCCESS_CODE = "0";
	/**
	 * 支付状态
	 */
	public static final String TRADING = "i";//交易中
	public static final String PAYMENT_SUCCESS = "s";////付款成功
	public static final String PAYMENT_FAILED = "f";//付款失败
	public static final String DOUBLE_PAYMENT  = "d";//重复支付
	public static final String REFUND_ORDER  = "r";//已退款
	/**
	 * 支付渠道 
	 */
	public static final String PAYTYPE_DOOOLY = "doooly";//兜礼积分
	public static final String PAYTYPE_WEIXIN  = "weixin";//对象微信APP
	public static final String PAYTYPE_WEIXIN_JSAPI  = "weixin_jsapi";//对应微信JSAPI
	
	//支付类型
//	short payType = (short)0;
//	String payTypeStr = payFlow.getPayType();
//	if(PayFlowService.PAYTYPE_DOOOLY.equals(payTypeStr)){
//		payType = PAY_TYPE_PLATFORM_POINT;
//	}else if(PayFlowService.PAYTYPE_WEIXIN.equals(payTypeStr)){
//		payType = PAY_TYPE_WECHAT_APP;
//	}else if(PayFlowService.PAYTYPE_WEIXIN_JSAPI.equals(payTypeStr)){
//		payType = PAY_TYPE_WECHAT_JSAPI;
//	}
	
	//支付类型
	public static enum PayType {
		// 参考表: ad_self_product_type
		DOOOLY(0, "doooly"),
		WEIXIN(3, "weixin"), 
		WEIXIN_JSAPI(10, "weixin_jsapi"), 
		OTHER(9, "weixin_jsapi"); 

		private int typeCode;
		private String typeName;
		
		private PayType(int typeCode, String typeName) {
			this.typeCode = typeCode;
			this.typeName = typeName;
		}
		public int getTypeCode() {
			return typeCode;
		}
		public void setTypeCode(int typeCode) {
			this.typeCode = typeCode;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		
		public static int getCodeByName(String name){
			 for (PayType e : PayType.values()) {
				 if(e.getTypeName().equals(name)){
					 return e.getTypeCode();
				 }
		     }
			 return OTHER.getTypeCode();
		}
		
	}
	
	
	PayFlow getByOrderNum(String orderNum,String payType,String payStatus);
	
	PayFlow getByTranNo(String transNo,String payType);
	
	PayFlow getById(String id);
	
	public int insert(PayFlow payFlow);
	
	public int update(PayFlow payFlow);
	
	public int updatePaySuccess(String flowId, String transNo);
	
}
