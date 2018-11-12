package com.doooly.business.myorder.po;

import java.util.Date;

public class OrderPoReq extends BasePo{
	private Long userId;
	private Date beginOrderDate;
	private Date endOrderDate;
	private Integer isUserRebate;
	private Integer type;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getBeginOrderDate() {
		return beginOrderDate;
	}
	public void setBeginOrderDate(Date beginOrderDate) {
		this.beginOrderDate = beginOrderDate;
	}
	public Date getEndOrderDate() {
		return endOrderDate;
	}
	public void setEndOrderDate(Date endOrderDate) {
		this.endOrderDate = endOrderDate;
	}
	public Integer getIsUserRebate() {
		return isUserRebate;
	}
	public void setIsUserRebate(Integer isUserRebate) {
		this.isUserRebate = isUserRebate;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	

}
