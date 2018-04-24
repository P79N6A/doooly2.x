package com.doooly.business.touristCard.datacontract.base;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public enum ResponseStatus {
	SUCCESS("200", "成功"),
	FAIL("300", "失败"),
	ERROR("400", "系统异常"),
	NOTFOUND("404", "未找到合法资源"),
	BADPARAM("405", "参数错误"),
	BADPARAM_REGION("406", "区域代码参数错误"),
	NOTREGIST("407", "手机号未注册"),
	PASSWORD_ERROR("408", "密码错误"),
	NOTLOGIN("409", "商家未登录"),
	DUPICATE("410", "数据重复"),
	VERIFY_SIGN_ERROR("502", "验签不一致");

	String code;
	String message;

	ResponseStatus(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String code() {
		return this.code;
	}

	public String message() {
		return this.message;
	}
}
