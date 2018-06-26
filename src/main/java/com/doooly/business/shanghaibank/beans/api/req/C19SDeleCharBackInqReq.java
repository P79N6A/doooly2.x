package com.doooly.business.shanghaibank.beans.api.req;


public class C19SDeleCharBackInqReq   {
	private String chargeCode;//代扣产品代码
	private String eAcctNo;//对公电子户帐号
	private String amount;//交易金额
	private String channelFlowNo;//第三方平台流水号
	private String channelId;//接入渠道Id
	private String usage;//用途
	private String platformSummary;//接入渠道Id
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public String getEAcctNo() {
		return eAcctNo;
	}
	public void setEAcctNo(String eAcctNo) {
		this.eAcctNo = eAcctNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getChannelFlowNo() {
		return channelFlowNo;
	}
	public void setChannelFlowNo(String channelFlowNo) {
		this.channelFlowNo = channelFlowNo;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getPlatformSummary() {
		return platformSummary;
	}
	public void setPlatformSummary(String platformSummary) {
		this.platformSummary = platformSummary;
	}
	
	
	
}
