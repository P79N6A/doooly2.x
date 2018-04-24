package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 商户积分分配
 * @author: qing.zhang
 * @date: 2018-03-07
 */
public class BusinessUserIntegral {
    private Long businessId;//'商户主表id'
    private Long userId;//'兜礼用户id'
    private Long businessMemberId;//'商家对应用户id'
    private BigDecimal businessIntegral;//'商户账户可用积分'
    private BigDecimal amount;//'积分交易数量'
    private Integer type;//积分类型 0：消费,1：收入;
    private Date createDate;//'创建时间'
    private Date updateDate;//'修改时间'
    private String remark;//备注

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBusinessMemberId() {
        return businessMemberId;
    }

    public void setBusinessMemberId(Long businessMemberId) {
        this.businessMemberId = businessMemberId;
    }

    public BigDecimal getBusinessIntegral() {
        return businessIntegral;
    }

    public void setBusinessIntegral(BigDecimal businessIntegral) {
        this.businessIntegral = businessIntegral;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
