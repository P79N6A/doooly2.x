package com.doooly.dto.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单返回状态信息
 * 
 * @author 2017-09-23 19:43:20 WANG
 *
 */
public class OrderMsg extends MessageDataBean {

	//you can define result code here
	//重复下单验证
	public static String unpaid_order_code = "2001";
	public static String unpaid_order_mess = "您有笔相同订单尚未支付，请勿重复提交，立即前往支付吧";
	//威尔士验证
	public static String wills_order_code = "2002";
	public static String wills_order_mess = "您的身份证已在该门店购买过，请勿重复下单";
	//优惠商品库存不足
	public static String out_of_stock_code1 = "2004";
	public static String out_of_stock_mess1 = "商品库存不足.";
	public static String out_of_stock_code2 = "2005";
	public static String out_of_stock_mess2 = "活动商品库存不足.";
	//活动结束
	public static String end_activity_code = "2007";
	public static String end_activity_mess = "活动已结束,订单不能支付.";
	//活动商品限购
	public static String purchase_limit_code = "2005";
	public static String purchase_limit_mess = "此商品只能购买%s件";//活动商品限购
	//创建订单失败,请稍后再试 - 扣除库存失败
	public static String create_order_failed_code = "2006";
	public static String create_order_failed_mess = "创建订单失败,请稍后再试";
	//抵扣券
	public static String invalid_coupon_code = "2008";
	public static String invalid_coupon_mess = "无效的抵扣券ID";
	//抵扣券金额大于售价
	public static String more_then_amount_code = "2009";
	public static String more_then_amount_mess = "抵扣券金额大于售价";
	//锁定券失败
	public static String lock_coupon_err_code = "2010";
	public static String lock_coupon_err_mess = "锁定券失败";
	//已锁定券
	public static String islocked_coupon_err_code = "2011";
	public static String islocked_coupon_err_mess = "券已被锁定无法使用";

	public OrderMsg() {
		super();
	}

	public OrderMsg(String success_code, String success_mess, Map<String, Object> data) {
		super(success_code, success_mess, data);
	}
	
	public OrderMsg(String success_code, String success_mess) {
		super(success_code, success_mess,new HashMap<String,Object>());
	}

}
