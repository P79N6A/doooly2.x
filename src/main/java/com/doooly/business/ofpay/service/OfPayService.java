package com.doooly.business.ofpay.service;

import java.util.Map;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.dto.common.PayMsg;

public interface OfPayService {

	public final static String OF_SUCCESS_CODE = "1";
	/**
	 * 手机充值
	 */
	public Map<String,String> mobileRecharge(OrderVo order) ;
	/**
	 * 流量充值
	 */
	public Map<String,String> flowRecharge(OrderVo order) ;
	/**
	 * 卡密 
	 */
	public Map<String,String> cardPswRecharge(OrderVo order) ;
	/**
	 * 检查手机号是否可以充值话费 
	 */
	public PayMsg telCheck(String phoneNo,String price) ;
	/**
	 * 检查手机号是否可以充值流量 
	 */	
	public PayMsg flowCheck(String phoneNo,String price) ;


	public PayMsg queryLeftcardNum(String cardid) ;



}
