package com.doooly.business.touristCard.datacontract.response;

import com.doooly.business.touristCard.datacontract.base.BaseResponse;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public class QueryAccountBalanceResponse extends BaseResponse {
	private String cardno;
	private String accountBalance;

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
}
