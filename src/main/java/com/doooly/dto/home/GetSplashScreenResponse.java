package com.doooly.dto.home;

import com.doooly.dto.base.BaseResponse;
import com.doooly.entity.home.SplashScreenDataContract;

/**
 * @className: GetSplashScreenResponse
 * @description: 兜礼APP闪屏页信息的响应封装类
 * @author: wangchenyu
 * @date: 2018-02-13 11:46
 */
public class GetSplashScreenResponse extends BaseResponse {
	/** 兜礼APP首页数据 **/
	private SplashScreenDataContract data;

	public SplashScreenDataContract getData() {
		return data;
	}

	public void setData(SplashScreenDataContract data) {
		this.data = data;
	}
}
