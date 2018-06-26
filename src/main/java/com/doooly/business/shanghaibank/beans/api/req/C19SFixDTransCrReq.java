package com.doooly.business.shanghaibank.beans.api.req;

public class C19SFixDTransCrReq {
	private String operatorId; // 操作员Id
	private String dynamicCode; // 动态验证码
	private String amount; // 转账金额
	private String channelId; // 发起渠道号
	private String reMark_1; // 备注1
	private String eAcctNo;//
	private String eAcctName;//
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
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
