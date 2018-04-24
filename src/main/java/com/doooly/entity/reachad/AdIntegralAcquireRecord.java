package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ad_integral_acquire_record用户领取积分活动明细表
 * 
 * @author yangwenwei
 * @date 2018年2月27日
 * @version 1.0
 */
public class AdIntegralAcquireRecord {
	private Long id;
	private Long userId;
	private Long integralActivityId;
	private BigDecimal integral;
	private Date createDate;
	private String integralName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getIntegralActivityId() {
		return integralActivityId;
	}
	public void setIntegralActivityId(Long integralActivityId) {
		this.integralActivityId = integralActivityId;
	}
	public BigDecimal getIntegral() {
		return integral;
	}
	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getIntegralName() {
		return integralName;
	}
	public void setIntegralName(String integralName) {
		this.integralName = integralName;
	}
	
	 
}