package com.doooly.dto.activity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class ActivityOrderRes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8772222144424173346L;

	private String code;
	
	private String msg;

	public ActivityOrderRes() {

	}
	
	public ActivityOrderRes(String code, String msg){
		this.code = code;
		this.msg = msg;
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

	@Override
	public String toString() {
		return "ActivityOrderRes [code=" + code + ", msg=" + msg + "]";
	}
	
	public String toJsonString(){
		JSONObject object = new JSONObject();
		object.put("code", code == null ? "" : code);
		object.put("msg", msg == null ? "" : msg);
		return object.toJSONString();
	}

}
