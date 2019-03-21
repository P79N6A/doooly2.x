package com.doooly.business.order.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 大订单表
 * @author: qing.zhang
 * @date: 2019-01-13
 */
public class AdOrderBig
{
    private String id;
    // 会员ID
    private Long userId;
    // 实付总金额
    private BigDecimal totalAmount;
    // 应付总金额
    private BigDecimal totalPrice;
    // 订单时间
    private Date orderDate;
    // 支付状态：0 未支付(进行中)   1已支付 2取消
    private int state;
    // 订单来源   0:机票个人      1：机票分销商       2：合作商家     3：睿渠平台
    private String isSource;
    // 备注信息
    private String remarks;
    // 更新时间
    private Date updateDate;
    // 创建时间
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAMount) {
        this.totalAmount = totalAMount;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIsSource() {
        return isSource;
    }

    public void setIsSource(String isSource) {
        this.isSource = isSource;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
