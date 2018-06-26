package com.doooly.business.shanghaibank.beans.api.req;

import java.util.List;


public class C19UnionPayLoanReq {

	private String loanFlowId;  //垫资流水号
	private String totalLoanAmt; //点资总金额
	private List<C19UnionPayLoanDetails>  unionPayLoanDetailsList;//垫资明细信息
	
	public String getLoanFlowId() {
		return loanFlowId;
	}
	public void setLoanFlowId(String loanFlowId) {
		this.loanFlowId = loanFlowId;
	}
	public String getTotalLoanAmt() {
		return totalLoanAmt;
	}
	public void setTotalLoanAmt(String totalLoanAmt) {
		this.totalLoanAmt = totalLoanAmt;
	}
	public List<C19UnionPayLoanDetails> getUnionPayLoanDetailsList() {
		return unionPayLoanDetailsList;
	}
	public void setUnionPayLoanDetailsList(
			List<C19UnionPayLoanDetails> unionPayLoanDetailsList) {
		this.unionPayLoanDetailsList = unionPayLoanDetailsList;
	}

	

}
