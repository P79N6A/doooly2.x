package com.doooly.entity.payment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 上海银行虚账户冻结解冻Entity
 * @author qing.zhang
 * @version 2018-05-28
 */
public class AdShanghaiBankFrozeRecord {

    private Long id;//主键
	private String eacctNo;		// 虚账户账号
	private String channelFlowNo;		// 兜礼代发流水号
	private Integer txnType;		// 交易类型(1虚账户冻结,2虚账户解冻)
	private BigDecimal applyAmount;		// 冻结或解冻金额
	private String notes;		// 备注
	private String platformSummary;		// 平台摘要
    private Date createDate;//创建时间
    private Date updateDate;//更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEacctNo() {
        return eacctNo;
    }

    public void setEacctNo(String eacctNo) {
        this.eacctNo = eacctNo;
    }

    public String getChannelFlowNo() {
        return channelFlowNo;
    }

    public void setChannelFlowNo(String channelFlowNo) {
        this.channelFlowNo = channelFlowNo;
    }

    public Integer getTxnType() {
        return txnType;
    }

    public void setTxnType(Integer txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPlatformSummary() {
        return platformSummary;
    }

    public void setPlatformSummary(String platformSummary) {
        this.platformSummary = platformSummary;
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