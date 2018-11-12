package com.doooly.business.myorder.vo;

import java.util.List;
import java.util.Map;

import com.doooly.business.myorder.dto.OrderResp;

public class OrderVoResp{
	
	private Integer totalPage;
	private Integer totalNum;
	private Integer currentPage;
	List<Map<String,String>> orderDataList;
	List<OrderResp> page;
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public List<Map<String, String>> getOrderDataList() {
		return orderDataList;
	}
	public void setOrderDataList(List<Map<String, String>> orderDataList) {
		this.orderDataList = orderDataList;
	}
	public List<OrderResp> getPage() {
		return page;
	}
	public void setPage(List<OrderResp> page) {
		this.page = page;
	}

	
}
