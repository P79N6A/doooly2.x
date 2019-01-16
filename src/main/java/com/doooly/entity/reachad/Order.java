package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * _order订单表pojo类
 * 
 * @author tangzhiyuan
 *
 */
public class Order {
	private Long id;

	private Long userid;

	private Long orderUserId;

	private String bussinessId;

	private String storesId;

	private String payPassword;

	private String verificationCode;

	private BigDecimal amount;

	private BigDecimal totalAmount;

	private BigDecimal price;

	private BigDecimal totalPrice;

	private Integer payType;

	private String orderNumber;

	private String serialNumber;

	private Date orderDate;

	private Integer state;

	private Integer orderType;

	private Integer type;

	private Integer source;

	private Integer isPayPassword;

	private Integer isRebate;

	private BigDecimal businessRebate;

	private BigDecimal userRebate;

	private BigDecimal cpsFee;

	private Date createDateTime;

	// 0：未对账 1：成功 2：错误 3：冗余 4：缺失
	private int checkState;

	private String orderDetail;
	
	private String orderDateStr;//订单时间字符串格式
	
	private String originOrderNumber;//订单时间字符串格式

    private Long bigOrderNumber;//大订单编号

	public Order() {

	}

	/** Setters and Getters **/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getOrderUserId() {
		return orderUserId;
	}

	public void setOrderUserId(Long orderUserId) {
		this.orderUserId = orderUserId;
	}

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public String getStoresId() {
		return storesId;
	}

	public void setStoresId(String storesId) {
		this.storesId = storesId;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getIsPayPassword() {
		return isPayPassword;
	}

	public void setIsPayPassword(Integer isPayPassword) {
		this.isPayPassword = isPayPassword;
	}

	public Integer getIsRebate() {
		return isRebate;
	}

	public void setIsRebate(Integer isRebate) {
		this.isRebate = isRebate;
	}

	public BigDecimal getBusinessRebate() {
		return businessRebate;
	}

	public void setBusinessRebate(BigDecimal businessRebate) {
		this.businessRebate = businessRebate;
	}

	public BigDecimal getUserRebate() {
		return userRebate;
	}

	public void setUserRebate(BigDecimal userRebate) {
		this.userRebate = userRebate;
	}

	public BigDecimal getCpsFee() {
		return cpsFee;
	}

	public void setCpsFee(BigDecimal cpsFee) {
		this.cpsFee = cpsFee;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}

	public String getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail == null ? null : orderDetail.trim();
	}

	public String getOrderDateStr() {
		return orderDateStr;
	}

	public void setOrderDateStr(String orderDateStr) {
		this.orderDateStr = orderDateStr;
	}

	public String getOriginOrderNumber() {
		return originOrderNumber;
	}

	public void setOriginOrderNumber(String originOrderNumber) {
		this.originOrderNumber = originOrderNumber;
	}

    public Long getBigOrderNumber() {
        return bigOrderNumber;
    }

    public void setBigOrderNumber(Long bigOrderNumber) {
        this.bigOrderNumber = bigOrderNumber;
    }
}
