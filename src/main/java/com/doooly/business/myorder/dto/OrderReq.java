package com.doooly.business.myorder.dto;

import com.doooly.common.dto.BaseReq;

public class OrderReq extends BaseReq<OrderReq> {
	
	private Integer type;
	private String userId ;
	private Integer currentPage;
	private Integer totalPage;
	private Integer pageSize;
	private Long businessId;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getCurrentPage() {
		if(this.currentPage == null || this.currentPage <=0) {
			this.currentPage = 1;
		}
		return currentPage;
	}
	
	public Integer getNextCurrentPage() {
		
		return getCurrentPage() + 1;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
	

}
