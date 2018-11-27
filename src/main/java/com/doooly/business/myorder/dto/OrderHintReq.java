package com.doooly.business.myorder.dto;

import com.doooly.common.dto.BaseReq;

public class OrderHintReq extends BaseReq<OrderHintReq> {
	
	private Long userId ;

	public Long getUserId() {
		return userId;
	}

	public String getHintState() {
		return hintState;
	}

	private String  hintState;

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setHintState(String hintState) {
		this.hintState = hintState;
	}
}
