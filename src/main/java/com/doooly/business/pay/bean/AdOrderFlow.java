package com.doooly.business.pay.bean;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sun.org.apache.xml.internal.utils.StringToStringTable;

public class AdOrderFlow {

	private Long id;

	private Long orderReportId;

	private String serialNumber;

	private Short payType;

	private BigDecimal amount;

	private String createBy;

	private String type;

	private String delFlag;

	private String remarks;

	private Date updateDate;

	private String updateBy;

	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderReportId() {
		return orderReportId;
	}

	public void setOrderReportId(Long orderReportId) {
		this.orderReportId = orderReportId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "AdOrderFlow{" +
				"id=" + id +
				", orderReportId=" + orderReportId +
				", serialNumber='" + serialNumber + '\'' +
				", payType=" + payType +
				", amount=" + amount +
				", createBy='" + createBy + '\'' +
				", type='" + type + '\'' +
				", delFlag='" + delFlag + '\'' +
				", remarks='" + remarks + '\'' +
				", updateDate=" + updateDate +
				", updateBy='" + updateBy + '\'' +
				", createDate=" + createDate +
				'}';
	}
}