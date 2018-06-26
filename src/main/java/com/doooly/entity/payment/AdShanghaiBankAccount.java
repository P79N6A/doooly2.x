package com.doooly.entity.payment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 上海银行开户Entity
 * @author qing.zhang
 * @version 2018-05-28
 */
public class AdShanghaiBankAccount {

    private Long id;//主键
	private String eacctNo;		// 虚账户账号
	private String eacctName;		// 虚账户户名
	private String subBranchNo;		// 开户归属支行号
	private String subBranchName;		// 开户归属支行名称
	private BigDecimal amount;		// 虚账户余额
	private BigDecimal frozenAmount;		// 虚账户冻结余额
    private Integer type;//开户类型 1，商户：2，企业
    private String businessId;		// 商家编号
    private Long groupId;		// 企业编号
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

    public String getEacctName() {
        return eacctName;
    }

    public void setEacctName(String eacctName) {
        this.eacctName = eacctName;
    }

    public String getSubBranchNo() {
        return subBranchNo;
    }

    public void setSubBranchNo(String subBranchNo) {
        this.subBranchNo = subBranchNo;
    }

    public String getSubBranchName() {
        return subBranchName;
    }

    public void setSubBranchName(String subBranchName) {
        this.subBranchName = subBranchName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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