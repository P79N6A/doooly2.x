package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

public class UserIntegral {
    private Long id;

    private Long adUserId;

    private Long batchNo;

    private BigDecimal totalIntegral;

    private BigDecimal availIntegral;

    private Byte type;

    private Byte sourceId;

    private Date createDate;

    private Date updateDate;

    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(Long adUserId) {
        this.adUserId = adUserId;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(BigDecimal totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public BigDecimal getAvailIntegral() {
        return availIntegral;
    }

    public void setAvailIntegral(BigDecimal availIntegral) {
        this.availIntegral = availIntegral;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getSourceId() {
        return sourceId;
    }

    public void setSourceId(Byte sourceId) {
        this.sourceId = sourceId;
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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }
}