package com.doooly.common.dto;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.Constants.ResponseCode;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "BaseRes")
public class BaseRes<T> implements Serializable {

	private static final long serialVersionUID = 1905835133216055926L;
	/**
	 * 消息响应结果代码
	 */
	private String code;
	/**
	 * 消息响应结果描述
	 */
	private String msg;
	// 接口返回业务数据
	private T data;

	public BaseRes() {
		super();
	}

	public BaseRes(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public BaseRes(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setSuccessBaseResponse() {
		setCode(ResponseCode.SUCCESS.getCode());
		setMsg(ResponseCode.SUCCESS.getDesc());
	}

	public void setServerInternalErrorResponse() {
		setCode(ResponseCode.SERVER_INTERNAL_ERROR.getCode());
		setMsg(ResponseCode.SERVER_INTERNAL_ERROR.getDesc());
	}

	public void setSecretKeyFailureResponse() {
		setCode(ResponseCode.SECRET_KEY_FAILURE.getCode());
		setMsg(ResponseCode.SECRET_KEY_FAILURE.getDesc());
	}

	public void setServerUnkownErrorResponse() {
		setCode(ResponseCode.SERVER_UNKOWN_ERROR.getCode());
		setMsg(ResponseCode.SERVER_UNKOWN_ERROR.getDesc());
	}

	public String toJsonString() {
		T data = this.data;
		JSONObject json = new JSONObject();
		json.put("code", this.code);
		json.put("msg", this.msg);
		json.put("data", data);
		return json.toJSONString();
	}
}
