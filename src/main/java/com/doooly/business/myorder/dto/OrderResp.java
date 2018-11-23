package com.doooly.business.myorder.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderResp {
	
	// 订单id
	private Long orderId;
	//订单编号
	private String orderNumber;
	// 会员ID
	private Long userId;
	//交易阶段 1：完成订单    5：退货订单, 自营订单状态(10:待付款,20:待发货,30已发货,40已收货,50确认收货,99交易取消, 交易完成和退款使用原来状态1,5)
	private String  state;
	//是否返利 0:是   1：否
	private Integer isUserRebate;
	//预返积分
	private Double userRebate;
	//实付金额
	private BigDecimal payAmount;
	//应付金额
	private BigDecimal amountPayable;
	//订单日期
	private String orderDate;
	//商户名称
	private String storeName;
	//订单来源   0:机票个人      1：机票分销商       2：合作商家     3：睿渠平台
	private Integer isSource;
	private Integer productType;
	private String logo;
	private String businessId;
	private String type;
	private BigDecimal savePrice;
	private String specification;
	private String goods;
	private String orderDateStr;
	private String company;
	private String productImg;
	private String integrateReturnDate;
	private String cashDeskSource;
	
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getIsUserRebate() {
		return isUserRebate;
	}
	public void setIsUserRebate(Integer isUserRebate) {
		this.isUserRebate = isUserRebate;
	}
	public Double getUserRebate() {
		return userRebate;
	}
	public void setUserRebate(Double userRebate) {
		this.userRebate = userRebate;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public BigDecimal getAmountPayable() {
		return amountPayable;
	}
	public void setAmountPayable(BigDecimal amountPayable) {
		this.amountPayable = amountPayable;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Integer getIsSource() {
		return isSource;
	}
	public void setIsSource(Integer isSource) {
		this.isSource = isSource;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getSavePrice() {
		return savePrice;
	}
	public void setSavePrice(BigDecimal savePrice) {
		this.savePrice = savePrice;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getOrderDateStr() {
		return orderDateStr;
	}
	public void setOrderDateStr(String orderDateStr) {
		this.orderDateStr = orderDateStr;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	public String getIntegrateReturnDate() {
		return integrateReturnDate;
	}
	public void setIntegrateReturnDate(String integrateReturnDate) {
		this.integrateReturnDate = integrateReturnDate;
	}
	public String getCashDeskSource() {
		return cashDeskSource;
	}
	public void setCashDeskSource(String cashDeskSource) {
		this.cashDeskSource = cashDeskSource;
	}
	

}
