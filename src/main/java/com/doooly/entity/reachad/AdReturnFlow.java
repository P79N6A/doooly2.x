package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品退货订单 退货流水 entity
 * 
 * @author yuelou.zhang
 * @version 2017年11月13日
 */
public class AdReturnFlow {
	private Long id;

	private Long orderReportId;

	private String returnFlowNumber;

	private String returnFlowSerialnumber;

	private Short payType;

	private BigDecimal amount;

	private String type;

	private BigDecimal userRebate;

	private BigDecimal userReturnAmount;

	private BigDecimal businessRebateAmount;

	private String billingState;

	private String createBy;

	private String delFlag;

	private Integer isFirst;

	private Integer isSource;

	private Integer firstCount;

	private BigDecimal airSettleAccounts;

	private String remarks;

	private Date updateDate;

	private String updateBy;

	private Date createDate;
	
	private List<AdReturnDetail> details;

	private static final long serialVersionUID = 1L;

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

	public String getReturnFlowNumber() {
		return returnFlowNumber;
	}

	public void setReturnFlowNumber(String returnFlowNumber) {
		this.returnFlowNumber = returnFlowNumber == null ? null : returnFlowNumber.trim();
	}

	public String getReturnFlowSerialnumber() {
		return returnFlowSerialnumber;
	}

	public void setReturnFlowSerialnumber(String returnFlowSerialnumber) {
		this.returnFlowSerialnumber = returnFlowSerialnumber == null ? null : returnFlowSerialnumber.trim();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public BigDecimal getUserRebate() {
		return userRebate;
	}

	public void setUserRebate(BigDecimal userRebate) {
		this.userRebate = userRebate;
	}

	public BigDecimal getUserReturnAmount() {
		return userReturnAmount;
	}

	public void setUserReturnAmount(BigDecimal userReturnAmount) {
		this.userReturnAmount = userReturnAmount;
	}

	public BigDecimal getBusinessRebateAmount() {
		return businessRebateAmount;
	}

	public void setBusinessRebateAmount(BigDecimal businessRebateAmount) {
		this.businessRebateAmount = businessRebateAmount;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState == null ? null : billingState.trim();
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy == null ? null : createBy.trim();
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag == null ? null : delFlag.trim();
	}

	public Integer getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Integer isFirst) {
		this.isFirst = isFirst;
	}

	public Integer getIsSource() {
		return isSource;
	}

	public void setIsSource(Integer isSource) {
		this.isSource = isSource;
	}

	public Integer getFirstCount() {
		return firstCount;
	}

	public void setFirstCount(Integer firstCount) {
		this.firstCount = firstCount;
	}

	public BigDecimal getAirSettleAccounts() {
		return airSettleAccounts;
	}

	public void setAirSettleAccounts(BigDecimal airSettleAccounts) {
		this.airSettleAccounts = airSettleAccounts;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks == null ? null : remarks.trim();
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
		this.updateBy = updateBy == null ? null : updateBy.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<AdReturnDetail> getDetails() {
		return details;
	}

	public void setDetails(List<AdReturnDetail> details) {
		this.details = details;
	}
	
}