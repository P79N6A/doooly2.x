package com.doooly.business.shanghaibank.beans.api.req;


public class C19AccPayDetailsFileInqReq {

	private String eAcctNo;  //对公电子户账号
	private String startDate;  //查询开始日期
	private String endDate;  //查询结束日期

	public String getEAcctNo() {
		return eAcctNo;
	}

	public void setEAcctNo(String eAcctNo) {
		this.eAcctNo = eAcctNo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	

}
