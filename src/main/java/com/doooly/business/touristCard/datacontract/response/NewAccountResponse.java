package com.doooly.business.touristCard.datacontract.response;

import com.doooly.business.touristCard.datacontract.base.BaseResponse;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public class NewAccountResponse extends BaseResponse {
	private String cardno;
	private String phoneno;
	private String userName;
	private String userID;
	private String userAddress;
	private String validate;
	private String issuers;

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getIssuers() {
		return issuers;
	}

	public void setIssuers(String issuers) {
		this.issuers = issuers;
	}
}
