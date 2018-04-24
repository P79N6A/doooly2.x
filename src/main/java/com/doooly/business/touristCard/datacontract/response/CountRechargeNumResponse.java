package com.doooly.business.touristCard.datacontract.response;

import com.doooly.business.touristCard.datacontract.base.BaseResponse;

/**
 * Created by 王晨宇 on 2018/1/22.
 */
public class CountRechargeNumResponse extends BaseResponse {
	/** 充值次数 **/
	private int totalNum;

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
}
