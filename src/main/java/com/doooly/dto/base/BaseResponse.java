package com.doooly.dto.base;

import com.doooly.common.DooolyResponseStatus;

import java.io.Serializable;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public class BaseResponse implements Serializable {
	private Integer code;
	private String msg;
	private Object originalRes;

	public BaseResponse() {
	}

	public BaseResponse setStatus(DooolyResponseStatus status){
		this.code = status.getCode();
		this.msg = status.getMsg();
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getOriginalRes() {
		return originalRes;
	}

	public void setOriginalRes(Object originalRes) {
		this.originalRes = originalRes;
	}
}
