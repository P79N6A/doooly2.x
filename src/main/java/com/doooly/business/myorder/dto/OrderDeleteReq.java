package com.doooly.business.myorder.dto;

public class OrderDeleteReq {

	private Long orderId;

	private Long userId;

	private String hintState;

	public void setHintState(String hintState) {
		this.hintState = hintState;
	}

	public String getHintState() {
		return hintState;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}


	
}
