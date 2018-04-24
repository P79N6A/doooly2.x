package com.doooly.common.dto;

import java.io.Serializable;

public class BaseAuth implements Serializable {
	private static final long serialVersionUID = -2538200853842273723L;

	/** 认证token */
	private String token;

	/** 执行时间 */
	private String execTime;

	public BaseAuth(String token){
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

}
