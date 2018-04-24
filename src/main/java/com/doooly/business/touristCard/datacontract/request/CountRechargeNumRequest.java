package com.doooly.business.touristCard.datacontract.request;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.PropertiesConstants;

/**
 * Created by 王晨宇 on 2018/1/17.
 */
public class CountRechargeNumRequest {
	private String userId;
	private String businessId;
	private String productType;
	private String cardno;

	public CountRechargeNumRequest(JSONObject json) {
		this.userId = json.getString("userId");
		this.businessId = PropertiesConstants.sctcdBundle.getString("SCTCD_MERCHANT_ID");
		this.productType = PropertiesConstants.sctcdBundle.getString("SCTCD_PRODUCT_TYPE");
		this.cardno = json.getString("cardno");
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
}
