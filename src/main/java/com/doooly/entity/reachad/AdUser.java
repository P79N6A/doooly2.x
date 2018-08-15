package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

public class AdUser {

	/** 会员类型：员工 */
	public static final int TYPE_EMPLOYEE = 0;

	/** 会员类型：家属 */
	public static final int TYPE_FAMILY = 1;

	/** 数据同步：同步 */
	public static final String DATA_SYN_ON = "1";
	/** 数据同步：同步 */
	public static final String DATA_SYN_OFF = "0";
	/**
	 * 会员激活标识
	 */
	public static final String USER_ACTIVATION_ON = "2";
	/**
	 * 会员未激活标识
	 */
	public static final String USER_ACTIVATION_OFF = "1";

	private Long id;

	private Long groupNum;

	private String name;

	private String sex;

	private String telephone;

	private String mailbox;

	private String identityCard;

	private String cardNumber;

	private String password;

	private String payPassword;

	private BigDecimal integral;

	private BigDecimal startIntegral;

	private BigDecimal expectIntegral;

	private BigDecimal lineCredit;

	private String isActive;

	private String isPayPassword;

	private Short type;

	private String dataSyn;

	private String createBy;

	private String delFlag;

	private Integer state;

	private String remarks;

	private String sourceCardNumber;

	private Date activeDate;

	private Date updateDate;

	private String updateBy;

	private Date createDate;
	private Integer dataSources;// '数据来源 0:平台导入，1：员工自主申请',
	private Integer isAudit;// '审核状态 0：审核中，1：审核通过，2：审核不通过',
	private String departmentId;// 部门id
	private Integer blocId;
	/** 会员企业 */
	private AdGroup adGroup;

	/** 激活码对象 */
	private AdActiveCode adActiveCode;
	/** 个人信息 */
	private AdUserPersonalInfo personalInfo;

	/** 旧手机号 */
	private String oldTelephone;

	/** 会员同步标志-true:符合同步条件;false:不符合同步条件 */
	private String syncFlag;

	/** 会员同步起始时间 */
	private String syncBeginDate;

	/** 激活时间dateStr */
	private String activeDateStr;

	public String getActiveDateStr() {
		return activeDateStr;
	}

	public void setActiveDateStr(String activeDateStr) {
		this.activeDateStr = activeDateStr;
	}

	public String getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public String getSyncBeginDate() {
		return syncBeginDate;
	}

	public void setSyncBeginDate(String syncBeginDate) {
		this.syncBeginDate = syncBeginDate;
	}

	public AdGroup getAdGroup() {
		return adGroup;
	}

	public void setAdGroup(AdGroup adGroup) {
		this.adGroup = adGroup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(Long groupNum) {
		this.groupNum = groupNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone == null ? null : telephone.trim();
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox == null ? null : mailbox.trim();
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard == null ? null : identityCard.trim();
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber == null ? null : cardNumber.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword == null ? null : payPassword.trim();
	}

	public BigDecimal getIntegral() {
		return integral;
	}

	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}

	public BigDecimal getStartIntegral() {
		return startIntegral;
	}

	public void setStartIntegral(BigDecimal startIntegral) {
		this.startIntegral = startIntegral;
	}

	public BigDecimal getExpectIntegral() {
		return expectIntegral;
	}

	public void setExpectIntegral(BigDecimal expectIntegral) {
		this.expectIntegral = expectIntegral;
	}

	public BigDecimal getLineCredit() {
		return lineCredit;
	}

	public void setLineCredit(BigDecimal lineCredit) {
		this.lineCredit = lineCredit;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive == null ? null : isActive.trim();
	}

	public String getIsPayPassword() {
		return isPayPassword;
	}

	public void setIsPayPassword(String isPayPassword) {
		this.isPayPassword = isPayPassword == null ? null : isPayPassword.trim();
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getDataSyn() {
		return dataSyn;
	}

	public void setDataSyn(String dataSyn) {
		this.dataSyn = dataSyn == null ? null : dataSyn.trim();
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks == null ? null : remarks.trim();
	}

	public String getSourceCardNumber() {
		return sourceCardNumber;
	}

	public void setSourceCardNumber(String sourceCardNumber) {
		this.sourceCardNumber = sourceCardNumber == null ? null : sourceCardNumber.trim();
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

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public AdActiveCode getAdActiveCode() {
		return adActiveCode;
	}

	public void setAdActiveCode(AdActiveCode adActiveCode) {
		this.adActiveCode = adActiveCode;
	}

	public AdUserPersonalInfo getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(AdUserPersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}

	public Integer getDataSources() {
		return dataSources;
	}

	public void setDataSources(Integer dataSources) {
		this.dataSources = dataSources;
	}

	public Integer getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getBlocId() {
		return blocId;
	}

	public void setBlocId(Integer blocId) {
		this.blocId = blocId;
	}

	public String getOldTelephone() {
		return oldTelephone;
	}

	public void setOldTelephone(String oldTelephone) {
		this.oldTelephone = oldTelephone;
	}

}