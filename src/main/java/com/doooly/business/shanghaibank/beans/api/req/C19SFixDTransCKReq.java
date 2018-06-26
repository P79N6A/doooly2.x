package com.doooly.business.shanghaibank.beans.api.req;

public class C19SFixDTransCKReq {
	private String operatorId; // 操作员Id
	private String dynamicCode; // 动态验证码
	private String eAcctNo;//
	private String eAcctName;//
	private String channelID; // 发起渠道号
	private String serialNo; // 单据编号
	private String reMark_1; // 备注1
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

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getReMark_1() {
		return reMark_1;
	}

	public void setReMark_1(String reMark_1) {
		this.reMark_1 = reMark_1;
	}

	public String getEAcctNo() {
		return eAcctNo;
	}

	public void setEAcctNo(String eAcctNo) {
		this.eAcctNo = eAcctNo;
	}

	public String getEAcctName() {
		return eAcctName;
	}

	public void setEAcctName(String eAcctName) {
		this.eAcctName = eAcctName;
	}
}
