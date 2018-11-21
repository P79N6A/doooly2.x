/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.business.myorder.po;

import java.math.BigDecimal;
import java.util.Date;

/**
 */
public class OrderDetailReport {

    private long id;//主键
	private Long orderId; // 订单编号
	private String orderNumber;
	private BigDecimal totalMount;
	private BigDecimal totalPrice;
	private Date orderDate;
	private String state; // 订单完成状态
	private Integer type; // 交易阶段
	private Integer isUserRebate;
	private BigDecimal userRebate;
	private BigDecimal userReturnAmount;
	private String isBusinessRebate;
	private BigDecimal businessRebateAmount;
	private String billingState;
	
	private String delFlag;
	private Integer isSource;
	private String company;
	private String logo;
	private BigDecimal savePrice;
	private Integer productType;
	private String consigneeMobile;
	private BigDecimal serviceCharge;
	private BigDecimal voucher;
	private Long userId;
    private String storeName;
    private String businessId;
    private String remarks;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public BigDecimal getTotalMount() {
		return totalMount;
	}
	public void setTotalMount(BigDecimal totalMount) {
		this.totalMount = totalMount;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIsUserRebate() {
		return isUserRebate;
	}
	public void setIsUserRebate(Integer isUserRebate) {
		this.isUserRebate = isUserRebate;
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
	public String getIsBusinessRebate() {
		return isBusinessRebate;
	}
	public void setIsBusinessRebate(String isBusinessRebate) {
		this.isBusinessRebate = isBusinessRebate;
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
		this.billingState = billingState;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Integer getIsSource() {
		return isSource;
	}
	public void setIsSource(Integer isSource) {
		this.isSource = isSource;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public BigDecimal getSavePrice() {
		return savePrice;
	}
	public void setSavePrice(BigDecimal savePrice) {
		this.savePrice = savePrice;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	public String getConsigneeMobile() {
		return consigneeMobile;
	}
	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}
	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public BigDecimal getVoucher() {
		return voucher;
	}
	public void setVoucher(BigDecimal voucher) {
		this.voucher = voucher;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

 
}