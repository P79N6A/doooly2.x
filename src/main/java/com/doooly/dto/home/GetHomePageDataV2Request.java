package com.doooly.dto.home;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.base.BaseRequest;

/**
 * @className: GetHomePageDataV2Request
 * @description:
 * @author: wangchenyu
 * @date: 2018-02-13 11:45
 */
public class GetHomePageDataV2Request extends BaseRequest {
	private Integer userId;

	public GetHomePageDataV2Request() {
	}

	public GetHomePageDataV2Request(JSONObject json) {
		this.userId = json.getInteger("userId");
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
