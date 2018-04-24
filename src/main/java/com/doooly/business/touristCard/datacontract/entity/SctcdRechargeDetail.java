package com.doooly.business.touristCard.datacontract.entity;

import com.doooly.business.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 王晨宇 on 2018/1/18.
 */
public class SctcdRechargeDetail {
	private BigDecimal rechargeAmount;
	private String createDate;
	private String result;

	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		if (createDate == null) {
			this.createDate = "";
		} else {
			this.createDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		}
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
