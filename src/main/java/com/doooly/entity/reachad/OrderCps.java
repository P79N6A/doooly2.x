package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * _order_cps订单营销费用表pojo类
 * 
 * @author tangzhiyuan
 *
 */
public class OrderCps {
	private Long id;
	// 订单类型 1：完成订单 5：退货订单
	private int orderType;
	// 营销补贴费用
	private BigDecimal cpsFee;
	// 会员id编号
	private Long userId;
	// 商家编号ID
	private Long businessId;
	// 商家订单号
	private String orderNumber;
	// 用户在该商家的第几笔符合cps规则的订单，0：不符合规则
	private Integer cpsNumber;
	private Date createDate;
	private Date updateDate;
	private int delFlag;

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getCpsFee() {
		return cpsFee;
	}

	public void setCpsFee(BigDecimal cpsFee) {
		this.cpsFee = cpsFee;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getCpsNumber() {
		return cpsNumber;
	}

	public void setCpsNumber(Integer cpsNumber) {
		this.cpsNumber = cpsNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
}
