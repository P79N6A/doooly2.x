package com.doooly.business.shanghaibank.beans.api.req;

public class C19SFixDDrCKReq {
	private String operatorId; // 操作员Id
	private String dynamicCode; // 动态验证码
	private String serialNo; // 单据编号

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getDynamicCode() {
		return dynamicCode;
	}

	public void setDynamicCode(String dynamicCode) {
		this.dynamicCode = dynamicCode;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

}
