package com.doooly.business.myorder.dto;

public class HintResp {
	
	private boolean newOrderFlag;
	private boolean newFinishFlag;
	private boolean newCancelFlag;
	private boolean recentlyPlacedOrderFlag; // 最近下单
	private boolean pendingPaymentFlag;		 // 待付款
	private boolean recentArrivalFlag;		 // 最近到账积分
	private boolean imminentArrivalFlag;	 // 即将到账
	
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

	public boolean isRecentlyPlacedOrderFlag() {
		return recentlyPlacedOrderFlag;
	}

	public void setRecentlyPlacedOrderFlag(boolean recentlyPlacedOrderFlag) {
		this.recentlyPlacedOrderFlag = recentlyPlacedOrderFlag;
	}

	public boolean isPendingPaymentFlag() {
		return pendingPaymentFlag;
	}

	public void setPendingPaymentFlag(boolean pendingPaymentFlag) {
		this.pendingPaymentFlag = pendingPaymentFlag;
	}

	public boolean isRecentArrivalFlag() {
		return recentArrivalFlag;
	}

	public void setRecentArrivalFlag(boolean recentArrivalFlag) {
		this.recentArrivalFlag = recentArrivalFlag;
	}

	public boolean isImminentArrivalFlag() {
		return imminentArrivalFlag;
	}

	public void setImminentArrivalFlag(boolean imminentArrivalFlag) {
		this.imminentArrivalFlag = imminentArrivalFlag;
	}
}
