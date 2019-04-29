package com.doooly.entity.reachad;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 积分消费记录Entity
 * @author sfc
 * @version 2019-04-02
 */
public class AdOrderIntegralRecord {
	
	private static final long serialVersionUID = 1L;

	private String bigOrderNumber;		// 大订单号
	private String orderNumber;		// 商品订单号
	private String userId;				//会员ID
	private BigDecimal orderAmount;		// 订单总金额
	private BigDecimal integralRebateAmount;		// 扣减批次积分金额
	private String batchNo;		// 批次号
	private Integer status;		// 状态(默认0目前没用到)
	private Integer type;		// 积分类型(1是定向积分2是通用积分)
	private Integer paymentType;		// 收款类型 0 商品，1 手续费
	private Integer flowType;		// 流水类型 0 支付 1 退款
	private Date createDate;
	private Date updateDate;

	public String getBigOrderNumber() {
		return bigOrderNumber;
	}

	public void setBigOrderNumber(String bigOrderNumber) {
		this.bigOrderNumber = bigOrderNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getIntegralRebateAmount() {
		return integralRebateAmount;
	}

	public void setIntegralRebateAmount(BigDecimal integralRebateAmount) {
		this.integralRebateAmount = integralRebateAmount;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
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
}