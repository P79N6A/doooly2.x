package com.doooly.entity.reachad;

import java.math.BigDecimal;

public class AdUserIntegral {
	private Long id;
	private Long adUserId;
	//该批次号总定向积分
	private BigDecimal totalIntegral;
	//该批次号可用定向积分
	private BigDecimal availIntegra;
	//企业充值定向积分批次号
	private String batchNo;
	//0:通用积分 1：定向积分
	private Integer type;
	//
	private Integer sourceId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAdUserId() {
		return adUserId;
	}
	public void setAdUserId(Long adUserId) {
		this.adUserId = adUserId;
	}
	public BigDecimal getTotalIntegral() {
		return totalIntegral;
	}
	public void setTotalIntegral(BigDecimal totalIntegral) {
		this.totalIntegral = totalIntegral;
	}
	public BigDecimal getAvailIntegra() {
		return availIntegra;
	}
	public void setAvailIntegra(BigDecimal availIntegra) {
		this.availIntegra = availIntegra;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
}
