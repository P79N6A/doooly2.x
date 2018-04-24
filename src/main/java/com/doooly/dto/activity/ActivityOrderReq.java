package com.doooly.dto.activity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author 赵清江
 * @date 2016年12月16日
 * @version 1.0
 */
public class ActivityOrderReq implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783045219249524814L;

	private String businessId;
	
	private String storeId;
	
	private String orderNumber;
	
	private String cardNumber;
	
	private String telephone;
	
	private Double totalPrice;
	
	private Double totalAmount;
	
	private Date orderDate;
	
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "ActivityOrderReq [businessId=" + businessId + ", storeId=" + storeId + ", orderNumber=" + orderNumber
				+ ", cardNumber=" + cardNumber + ", telephone=" + telephone + ", totalPrice=" + totalPrice
				+ ", totalAmount=" + totalAmount + ", orderDate=" + orderDate + "]";
	}
}
