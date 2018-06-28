package com.doooly.entity.payment;

import java.util.Date;

/**
 * 供应商开户表Entity
 * @author qing.zhang
 * @version 2018-05-31
 */
public class AdBankBusinessOpenAccount {
	private Integer id;//主键
    private Integer businessNo;

    private String businessName;

    private String eacctName;

    private String eacctNo;

    private String subBranchNo;

    private String subBranchName;

    private Byte eacctStatus;

    private String businessEntityCard;

    private Byte businessEntityCardStatus;

    private String bankName;

    private String accountBank;//银行行号

    private String openAccountBankName;

    private String cardholderName;

    private String createBy;

    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(Integer businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEacctName() {
        return eacctName;
    }

    public void setEacctName(String eacctName) {
        this.eacctName = eacctName;
    }

    public String getEacctNo() {
        return eacctNo;
    }

    public void setEacctNo(String eacctNo) {
        this.eacctNo = eacctNo;
    }

    public String getSubBranchNo() {
        return subBranchNo;
    }

    public void setSubBranchNo(String subBranchNo) {
        this.subBranchNo = subBranchNo;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getSubBranchName() {
        return subBranchName;
    }

    public void setSubBranchName(String subBranchName) {
        this.subBranchName = subBranchName;
    }

    public Byte getEacctStatus() {
        return eacctStatus;
    }

    public void setEacctStatus(Byte eacctStatus) {
        this.eacctStatus = eacctStatus;
    }

    public String getBusinessEntityCard() {
        return businessEntityCard;
    }

    public void setBusinessEntityCard(String businessEntityCard) {
        this.businessEntityCard = businessEntityCard;
    }

    public Byte getBusinessEntityCardStatus() {
        return businessEntityCardStatus;
    }

    public void setBusinessEntityCardStatus(Byte businessEntityCardStatus) {
        this.businessEntityCardStatus = businessEntityCardStatus;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOpenAccountBankName() {
        return openAccountBankName;
    }

    public void setOpenAccountBankName(String openAccountBankName) {
        this.openAccountBankName = openAccountBankName;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}