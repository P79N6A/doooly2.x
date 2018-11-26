package com.doooly.business.myorder.po;

public class OrderDetailPoReq {

    private Long userId;
    private Long orderId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
