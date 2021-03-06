package com.doooly.business.touristCard.datacontract.request;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.touristCard.datacontract.base.BaseRequest;
import com.doooly.common.constants.PropertiesHolder;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by 王晨宇 on 2018/1/15.
 */
public class QueryAccountBalanceRequest extends BaseRequest {
	/** 卡号(11位卡面号) **/
	private String cardno;

	public QueryAccountBalanceRequest(JSONObject json) {
		super();
		this.requestType = PropertiesHolder.getProperty("ACCOUNT_BALANCE_SERVICE");
		String orginialCardNo = json.getString("cardno");
		if ( !StringUtils.isEmpty(orginialCardNo) && orginialCardNo.length() == 12) {
			this.cardno = orginialCardNo.substring(1, orginialCardNo.length());
		} else {
			this.cardno = orginialCardNo;
		}
	}

	@Override
	public boolean paramsNotEmpty() {
		if (StringUtils.isEmpty(cardno)) {
			return false;
		}
		return true;
	}

	@Override
	public String getParamsString() {
		return "requestType=" + requestType +
				"&cardno=" + cardno +
				"&merchantNum=" + merchantNum +
				"&requestDate=" + requestDate;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
}
