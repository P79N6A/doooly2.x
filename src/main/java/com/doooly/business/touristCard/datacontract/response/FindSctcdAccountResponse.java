package com.doooly.business.touristCard.datacontract.response;

import com.doooly.business.touristCard.datacontract.base.BaseResponse;
import com.doooly.business.touristCard.datacontract.entity.SctcdAccount;

import java.util.List;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public class FindSctcdAccountResponse extends BaseResponse {
	private List<SctcdAccount> sctcdAccountList;

	public List<SctcdAccount> getSctcdAccountList() {
		return sctcdAccountList;
	}

	public void setSctcdAccountList(List<SctcdAccount> sctcdAccountList) {
		this.sctcdAccountList = sctcdAccountList;
	}
}
