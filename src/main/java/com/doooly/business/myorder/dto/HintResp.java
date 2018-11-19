package com.doooly.business.myorder.dto;

public class HintResp {
	
	private boolean newOrderFlag;
	private boolean newFinishFlag;
	private boolean newCancelFlag;
	
	public boolean isNewOrderFlag() {
		return newOrderFlag;
	}
	public void setNewOrderFlag(boolean newOrderFlag) {
		this.newOrderFlag = newOrderFlag;
	}
	public boolean isNewFinishFlag() {
		return newFinishFlag;
	}
	public void setNewFinishFlag(boolean newFinishFlag) {
		this.newFinishFlag = newFinishFlag;
	}
	public boolean isNewCancelFlag() {
		return newCancelFlag;
	}
	public void setNewCancelFlag(boolean newCancelFlag) {
		this.newCancelFlag = newCancelFlag;
	}
	
	
}
