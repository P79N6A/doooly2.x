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
public class AdIntegralActivityConn {
	private Long id;
	private Long activityId;
	private Long integralId;
	private Date createDate;
	private BigDecimal integralForEach;
	private BigDecimal availableIntegral;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public Long getIntegralId() {
		return integralId;
	}
	public void setIntegralId(Long integralId) {
		this.integralId = integralId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public BigDecimal getIntegralForEach() {
		return integralForEach;
	}
	public void setIntegralForEach(BigDecimal integralForEach) {
		this.integralForEach = integralForEach;
	}
	public BigDecimal getAvailableIntegral() {
		return availableIntegral;
	}
	public void setAvailableIntegral(BigDecimal availableIntegral) {
		this.availableIntegral = availableIntegral;
	}
	
	 
}