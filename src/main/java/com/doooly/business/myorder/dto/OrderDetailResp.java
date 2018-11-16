package com.doooly.business.myorder.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetailResp {
	
	private long id;//主键
	private Long orderId; // 订单编号
	private String orderNumber;
	private BigDecimal payAmount;
	private BigDecimal amountPayable;
	private String orderDate;
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
	private String  goods;
	private String specification;
	private String cardCode;
	private String cardPass;
	private String productImg;
	private String duihuanUrl;
	private String retState;
	private String sctcdCardno;
	private String sctcdAccountMobile;
	private String sctcdAccountIdCard;
	private String payTypeStr;
	private String integrateReturnDate;
	private String businessId;
	private String groupShortName;
	private String systemDate;
	
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
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getCardCode() {
		return cardCode;
	}
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	public String getCardPass() {
		return cardPass;
	}
	public void setCardPass(String cardPass) {
		this.cardPass = cardPass;
	}
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	public String getDuihuanUrl() {
		return duihuanUrl;
	}
	public void setDuihuanUrl(String duihuanUrl) {
		this.duihuanUrl = duihuanUrl;
	}
	public String getRetState() {
		return retState;
	}
	public void setRetState(String retState) {
		this.retState = retState;
	}
	public String getSctcdCardno() {
		return sctcdCardno;
	}
	public void setSctcdCardno(String sctcdCardno) {
		this.sctcdCardno = sctcdCardno;
	}
	public String getSctcdAccountMobile() {
		return sctcdAccountMobile;
	}
	public void setSctcdAccountMobile(String sctcdAccountMobile) {
		this.sctcdAccountMobile = sctcdAccountMobile;
	}
	public String getSctcdAccountIdCard() {
		return sctcdAccountIdCard;
	}
	public void setSctcdAccountIdCard(String sctcdAccountIdCard) {
		this.sctcdAccountIdCard = sctcdAccountIdCard;
	}
	public String getPayTypeStr() {
		return payTypeStr;
	}
	public void setPayTypeStr(String payTypeStr) {
		this.payTypeStr = payTypeStr;
	}
	public String getIntegrateReturnDate() {
		return integrateReturnDate;
	}
	public void setIntegrateReturnDate(String integrateReturnDate) {
		this.integrateReturnDate = integrateReturnDate;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getGroupShortName() {
		return groupShortName;
	}
	public void setGroupShortName(String groupShortName) {
		this.groupShortName = groupShortName;
	}
	public String getSystemDate() {
		return systemDate;
	}
	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}

}
