package com.doooly.business.touristCard.datacontract.base;

import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PropertiesHolder;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public abstract class BaseRequest implements Serializable {
	/** 接口名称 **/
	public String requestType;
	/** 商户号 **/
	public String merchantNum;
	/** 请求时间，格式：yyyyMMddHHmmss **/
	public String requestDate;

	public BaseRequest() {
		this.merchantNum = PropertiesHolder.getProperty("SCTCD_MERCHANT_NUM");
		this.requestDate = DateUtils.getDate("yyyyMMddHHmmss");
	}

	public abstract boolean paramsNotEmpty();
	public abstract String getParamsString() throws UnsupportedEncodingException;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getMerchantNum() {
		return merchantNum;
	}

	public void setMerchantNum(String merchantNum) {
		this.merchantNum = merchantNum;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
}
