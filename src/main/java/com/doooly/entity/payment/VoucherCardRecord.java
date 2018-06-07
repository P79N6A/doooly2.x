package com.doooly.entity.payment;

import java.util.Date;

/**
 * @Auther: liangjun
 * @Date: 2018/5/29 10:09
 * @Description:
 */
public class VoucherCardRecord {

    /**未激活*/
    public static final int CARD_ACTIVATION_STATUS_0 = 0;
    /**已激活 */
    public static final int CARD_ACTIVATION_STATUS_1 = 1;
    /**已冻结*/
    public static final int CARD_ACTIVATION_STATUS_2 = 2;
    /**已作废*/
    public static final int CARD_ACTIVATION_STATUS_3 = 3;

    private String id;
    private Integer cardTypeId;
    private Integer cardId;
    private String cardName;
    private Integer cardSerialNo;
    private String cardNo;
    private String cardPassword;
    private Integer cardMoney;
    private String activationCode;
    private Integer activationCodeUseStatus;
    private String activationCodeUseUid;
    private Date activationCodeUseTime;
    private Integer cardActivationStatus;
    private Date cardActivationTime;
    private Integer cardUseStatus;
    private String cardUseUid;
    private String cardUseMobile;
    private Date cardUseTime;
    private String cardUseDate;
    private String remark;
    private Date beginTime;
    private Date endTime;
    private String batchNo;
    private Long applicationId;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
    private String groupId;
    private Integer applicationStatus;
    public VoucherCardRecord() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getCardSerialNo() {
        return cardSerialNo;
    }

    public void setCardSerialNo(Integer cardSerialNo) {
        this.cardSerialNo = cardSerialNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardPassword() {
        return cardPassword;
    }

    public void setCardPassword(String cardPassword) {
        this.cardPassword = cardPassword;
    }

    public Integer getCardMoney() {
        return cardMoney;
    }

    public void setCardMoney(Integer cardMoney) {
        this.cardMoney = cardMoney;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Integer getActivationCodeUseStatus() {
        return activationCodeUseStatus;
    }

    public void setActivationCodeUseStatus(Integer activationCodeUseStatus) {
        this.activationCodeUseStatus = activationCodeUseStatus;
    }

    public String getActivationCodeUseUid() {
        return activationCodeUseUid;
    }

    public void setActivationCodeUseUid(String activationCodeUseUid) {
        this.activationCodeUseUid = activationCodeUseUid;
    }

    public Date getActivationCodeUseTime() {
        return activationCodeUseTime;
    }

    public void setActivationCodeUseTime(Date activationCodeUseTime) {
        this.activationCodeUseTime = activationCodeUseTime;
    }

    public Integer getCardActivationStatus() {
        return cardActivationStatus;
    }

    public void setCardActivationStatus(Integer cardActivationStatus) {
        this.cardActivationStatus = cardActivationStatus;
    }

    public Date getCardActivationTime() {
        return cardActivationTime;
    }

    public void setCardActivationTime(Date cardActivationTime) {
        this.cardActivationTime = cardActivationTime;
    }

    public Integer getCardUseStatus() {
        return cardUseStatus;
    }

    public void setCardUseStatus(Integer cardUseStatus) {
        this.cardUseStatus = cardUseStatus;
    }

    public String getCardUseUid() {
        return cardUseUid;
    }

    public void setCardUseUid(String cardUseUid) {
        this.cardUseUid = cardUseUid;
    }

    public Date getCardUseTime() {
        return cardUseTime;
    }

    public void setCardUseTime(Date cardUseTime) {
        this.cardUseTime = cardUseTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCardUseMobile() {
		return cardUseMobile;
	}

	public void setCardUseMobile(String cardUseMobile) {
		this.cardUseMobile = cardUseMobile;
	}

	public String getCardUseDate() {
		return cardUseDate;
	}

	public void setCardUseDate(String cardUseDate) {
		this.cardUseDate = cardUseDate;
	}

	public Integer getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(Integer applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
    
}
