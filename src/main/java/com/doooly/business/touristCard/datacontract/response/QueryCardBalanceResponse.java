package com.doooly.business.touristCard.datacontract.response;

import com.doooly.business.touristCard.datacontract.base.BaseResponse;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public class QueryCardBalanceResponse extends BaseResponse {
	private String cardno;
	private String cardBalance;
	private String balance;
	/** 卡押金 **/
	private String deposit;
	/** 卡有效期：yyyyMMdd **/
	private String validate;

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCardBalance() {
		return cardBalance;
	}

	public void setCardBalance(String cardBalance) {
		this.cardBalance = cardBalance;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}
}
