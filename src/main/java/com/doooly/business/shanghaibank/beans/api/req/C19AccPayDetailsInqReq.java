package com.doooly.business.shanghaibank.beans.api.req;


public class C19AccPayDetailsInqReq {
	private String eAcctNo;  //对公电子户账号
	private String ChannelId; //接入渠道Id
	private String StartDate;  //查询开始日期
	private String EndDate;  //查询结束日期
	
	public String geteAcctNo() {
		return eAcctNo;
	}
	public void seteAcctNo(String eAcctNo) {
		this.eAcctNo = eAcctNo;
	}
	public String getChannelId() {
		return ChannelId;
	}
	public void setChannelId(String channelId) {
		ChannelId = channelId;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	
}
