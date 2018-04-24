package com.doooly.dto.home;

import com.alibaba.fastjson.JSONObject;

/**
 * @className: GuideFinishRequest
 * @description: 引导完成的request请求
 * @author: wangchenyu
 * @date: 2018-03-01 15:00
 */
public class GuideFinishRequest {
	private Integer userId;
	private Integer secretCode;

	public GuideFinishRequest() {
	}

	public GuideFinishRequest(JSONObject json) {
		this.userId = json.getInteger("userId");
		this.secretCode = json.getInteger("secretCode");
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(Integer secretCode) {
		this.secretCode = secretCode;
	}
}
