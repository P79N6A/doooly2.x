package com.doooly.entity.reachad;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AdRefundFlow implements Serializable {
    private Long id;

    private String orderNumber;

    private String payType;

    private String userId;

    private String oldTransNo;

    private String refundId;

    private BigDecimal amount;

    private BigDecimal applyAmount;

    private Date submitTime;

    private String refundStatus;

    private Date refundCallbackTime;

    private String errorCode;

    private String errorReason;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getOldTransNo() {
        return oldTransNo;
    }

    public void setOldTransNo(String oldTransNo) {
        this.oldTransNo = oldTransNo == null ? null : oldTransNo.trim();
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId == null ? null : refundId.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus == null ? null : refundStatus.trim();
    }

    public Date getRefundCallbackTime() {
        return refundCallbackTime;
    }

    public void setRefundCallbackTime(Date refundCallbackTime) {
        this.refundCallbackTime = refundCallbackTime;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : errorCode.trim();
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason == null ? null : errorReason.trim();
    }
}