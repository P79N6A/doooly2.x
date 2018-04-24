package com.doooly.dto.home;

import com.doooly.dto.base.BaseResponse;
import com.doooly.entity.home.UserGuideFlow;

/**
 * @className: UserGuideFlowResponse
 * @description: 用户的新手引导完成进度Response对象
 * @author: wangchenyu
 * @date: 2018-03-06 20:43
 */
public class UserGuideFlowResponse extends BaseResponse {
	private UserGuideFlow data;

	public UserGuideFlow getData() {
		return data;
	}

	public void setData(UserGuideFlow data) {
		this.data = data;
	}
}
