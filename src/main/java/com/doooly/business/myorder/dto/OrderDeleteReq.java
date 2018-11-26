package com.doooly.business.myorder.dto;

public class OrderDeleteReq {

	private Long orderId;

	private Long userId;

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
