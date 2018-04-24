package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ad_integral_activity积分管理表
 * 
 * @author yangwenwei
 * @date 2018年2月27日
 * @version 1.0
 */
public class AdIntegralActivity {
	private Long id;
	private String integralNum;
	private String  integralName;
	private BigDecimal totalIntegral;
	private BigDecimal availableIntegral;
	private String isOpen;
	private Date createDate;
	private Date updateDate;
	private String createBy;
	private String updateBy;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIntegralNum() {
		return integralNum;
	}
	public void setIntegralNum(String integralNum) {
		this.integralNum = integralNum;
	}
	public String getIntegralName() {
		return integralName;
	}
	public void setIntegralName(String integralName) {
		this.integralName = integralName;
	}
	public BigDecimal getTotalIntegral() {
		return totalIntegral;
	}
	public void setTotalIntegral(BigDecimal totalIntegral) {
		this.totalIntegral = totalIntegral;
	}
	public BigDecimal getAvailableIntegral() {
		return availableIntegral;
	}
	public void setAvailableIntegral(BigDecimal availableIntegral) {
		this.availableIntegral = availableIntegral;
	}
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	
}