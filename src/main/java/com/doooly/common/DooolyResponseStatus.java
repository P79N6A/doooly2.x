package com.doooly.common;

public enum DooolyResponseStatus {
	SUCCESS(1000, "成功"),
	FAIL(20000, "业务处理失败"),
	BAD_PARAMS(20001, "请求参数错误"),
	SYSTEM_ERROR(30000, "系统异常");


	private Integer code;
	private String msg;

	private DooolyResponseStatus(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
