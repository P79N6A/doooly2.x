package com.doooly.business.pay.bean;

import java.util.Date;

/**
 * @Description: 订单来源记录表
 * @author: qing.zhang
 * @date: 2018-09-10
 */
public class AdOrderSource {

    private Long id;

    private Long businessId;

    private String orderNumber;

    private String cashDeskSource;

    private String traceCodeSource;

    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCashDeskSource() {
        return cashDeskSource;
    }

    public void setCashDeskSource(String cashDeskSource) {
        this.cashDeskSource = cashDeskSource;
    }

    public String getTraceCodeSource() {
        return traceCodeSource;
    }

    public void setTraceCodeSource(String traceCodeSource) {
        this.traceCodeSource = traceCodeSource;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
