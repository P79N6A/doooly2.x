package com.doooly.dto.common;

import java.util.Map;

/**
 * 订单返回状态信息
 * 
 * @author 2017-09-23 19:43:20 WANG
 *
 */
public class PayMsg extends MessageDataBean {

	//you can define result code here.
	
	
	public  PayMsg() {
	}

	public PayMsg(String success_code, String success_mess, Map<String, Object> data) {
		super(success_code, success_mess, data);
	}
	
	public PayMsg(String success_code, String success_mess) {
		super(success_code, success_mess);
	}

	@Override
	public String toString() {
		return "PayMsg [code=" + code + ", mess=" + mess + ", data=" + data + "]";
	}
	
}
