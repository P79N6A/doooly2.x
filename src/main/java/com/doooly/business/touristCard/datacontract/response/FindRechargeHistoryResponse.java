package com.doooly.business.touristCard.datacontract.response;

import com.doooly.business.touristCard.datacontract.base.BaseResponse;

import java.util.List;

/**
 * Created by 王晨宇 on 2018/1/17.
 */
public class FindRechargeHistoryResponse<T> extends BaseResponse {
	private List<T> rechargeHistoryList;

	public List<T> getRechargeHistoryList() {
		return rechargeHistoryList;
	}

	public void setRechargeHistoryList(List<T> rechargeHistoryList) {
		this.rechargeHistoryList = rechargeHistoryList;
	}
}
