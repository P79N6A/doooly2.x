package com.doooly.dto.home;

import com.doooly.dto.base.BaseResponse;
import com.doooly.entity.home.HomePageDataV2;

/**
 * @className: GetHomePageDataV2Response
 * @description: 兜礼APP首页，数据响应对象
 * @author: wangchenyu
 * @date: 2018-02-13 11:46
 */
public class GetHomePageDataV2Response extends BaseResponse {
	/** 兜礼APP首页数据 **/
	private HomePageDataV2 data;

	public HomePageDataV2 getData() {
		return data;
	}

	public void setData(HomePageDataV2 data) {
		this.data = data;
	}
}
