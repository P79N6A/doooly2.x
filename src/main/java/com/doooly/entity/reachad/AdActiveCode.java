package com.doooly.entity.reachad;

import java.util.Date;

/**
 * ad_active_code激活码表POJO类
 * 
 * @author 赵清江
 * @date 2016年7月15日
 * @version 1.0
 */
public class AdActiveCode {
	/**
	 * 激活码已使用
	 */
	public static final String ACTIVE_CODE_USED = "1";
	/**
	 * 激活码未使用
	 */
	public static final String ACTIVE_CODE_NOT_USED = "0";

	private Long id;

	private Long adUserId;

	private String code;

	private String cardNumber;

	private String isUsed;

	private Date usedDate;

	private String createBy;

	private String delFlag;

	private String remarks;

	private Date updateDate;

	private String updateBy;

	private Date createDate;

	/** 会员编号 */
	private AdUser adUser;

	public AdUser getAdUser() {
		return adUser;
	}

	public void setAdUser(AdUser adUser) {
		this.adUser = adUser;
	}

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber == null ? null : cardNumber.trim();
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed == null ? null : isUsed.trim();
	}

	public Date getUsedDate() {
		return usedDate;
	}

	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy == null ? null : createBy.trim();
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag == null ? null : delFlag.trim();
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks == null ? null : remarks.trim();
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy == null ? null : updateBy.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}