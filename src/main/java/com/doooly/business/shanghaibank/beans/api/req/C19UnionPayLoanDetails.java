package com.doooly.business.shanghaibank.beans.api.req;


public class C19UnionPayLoanDetails {

	private String merchantCode;  //商户代码
	private String loanAmt;  //垫资金额
	private String loanNum;  //垫资笔数
	
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getLoanAmt() {
		return loanAmt;
	}
	public void setLoanAmt(String loanAmt) {
		this.loanAmt = loanAmt;
	}
	public String getLoanNum() {
		return loanNum;
	}
	public void setLoanNum(String loanNum) {
		this.loanNum = loanNum;
	}

}
