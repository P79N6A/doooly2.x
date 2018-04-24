package com.doooly.business.touristCard.datacontract.request;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.touristCard.datacontract.base.BaseRequest;
import com.doooly.common.constants.PropertiesHolder;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by 王晨宇 on 2018/1/15.
 */
public class VerifyAccountRequest extends BaseRequest {
	/** 卡号(11位卡面号) **/
	private String cardno;
	/** 手机号(11位手机号) **/
	private String phoneno;

	public VerifyAccountRequest(JSONObject json) {
		super();
		this.requestType = PropertiesHolder.getProperty("VERIFY_ACCOUNT_SERVICE");
		String orginialCardNo = json.getString("cardno");
		if ( !StringUtils.isEmpty(orginialCardNo) && orginialCardNo.length() == 12) {
			this.cardno = orginialCardNo.substring(1, orginialCardNo.length());
		} else {
			this.cardno = orginialCardNo;
		}
		this.phoneno = json.getString("phoneno");
	}

	@Override
	public boolean paramsNotEmpty() {
		if (StringUtils.isEmpty(cardno)
			|| StringUtils.isEmpty(phoneno)
		) {
			return false;
		}
		return true;
	}

	@Override
	public String getParamsString() {
		return "requestType=" + requestType +
				"&cardno=" + cardno +
				"&phoneno=" + phoneno +
				"&merchantNum=" + merchantNum +
				"&requestDate=" + requestDate;
	}

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
}
