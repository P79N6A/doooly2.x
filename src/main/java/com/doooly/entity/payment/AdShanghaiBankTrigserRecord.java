package com.doooly.entity.payment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 上海银行虚账户代发Entity
 * @author qing.zhang
 * @version 2018-05-28
 */
public class AdShanghaiBankTrigserRecord{

    private Long id;//主键
	private String eacctNo;		// 虚账户账号
	private String payAccount;		// 代发账号
	private String payAccountName;		// 代发账户名
	private BigDecimal amount;		// 代发金额
	private String channelFlowNo;		// 兜礼代发流水号
	private String bankFlowNo;		// 上海银行流水号
	private String useage;		// 用途
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

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayAccountName() {
        return payAccountName;
    }

    public void setPayAccountName(String payAccountName) {
        this.payAccountName = payAccountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getChannelFlowNo() {
        return channelFlowNo;
    }

    public void setChannelFlowNo(String channelFlowNo) {
        this.channelFlowNo = channelFlowNo;
    }

    public String getBankFlowNo() {
        return bankFlowNo;
    }

    public void setBankFlowNo(String bankFlowNo) {
        this.bankFlowNo = bankFlowNo;
    }

    public String getUseage() {
        return useage;
    }

    public void setUseage(String useage) {
        this.useage = useage;
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