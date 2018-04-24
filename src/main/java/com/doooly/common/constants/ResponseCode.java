package com.doooly.common.constants;

/**
 * DTO 响应返回码<br>
 * 返回码定义规范:<br> 
 * 参照http://test2.doooly.com:7001/#/index
 * @author 杨汶蔚
 * @date 2018年2月11日
 * @since 1.8
 * @version 1.0
 */
public enum ResponseCode {
	
	REQUEST_SUCCESS("0","请求成功"),
	REQUEST_FAILURE("30000","请求失败"),
	
	
	USER_NOT_EXIST("10001", "会员不存在"),
	WRONG_PASSWORD("10002", "会员密码错误"),
	SUCCESSNULL("10003", "请求成功但无数据");
	
	
	
	private String code;
	private String msg;
	
	private ResponseCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
