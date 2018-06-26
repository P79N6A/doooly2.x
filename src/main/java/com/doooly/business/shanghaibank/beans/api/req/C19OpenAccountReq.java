package com.doooly.business.shanghaibank.beans.api.req;

import java.math.BigDecimal;

public class C19OpenAccountReq {
	private String cNName;// 中文客户全称
	private String gBName;// 英文客户全称
	private String idNo;// 证件号码
	private String custType;// 客户类别
	private String trdCode;// 行业代码
	private String regId;// 注册登记号
	private String limitDate;// 注册期限
	private String corpName;// 法人代表姓名
	private String legPerId;// 法定代表人证件号
	private String corpIdCardLimitDate;// 法人证件到期日
	private String legalPhone;// 法人代表联系电话
	private String cfoName;// 财务主管姓名
	private String telephone;// 财务主管电话
	private BigDecimal capReg;// 注册资本
	private String medSmEntFlg;// 企业规模
	private String regDist;// 省市区街道代码
	private String regStrentDoor;// 街道地址（具体到门牌号）
	private String regTePhone;// 注册地址电话号码
	private String managerName;// 管理员姓名
	private String managerMobile;// 管理员手机号
	private String managerIdCard;// 管理员身份证
	private String acctBank;// 绑定卡清算行行号
	private String account;// 绑定银行对公账号
	private String acctName;// 绑定银行对公账号户名
	private String depType;// 存款人类别
	private String regType;// 注册类型
	private String docuOpName;// 制单员姓名
	private String docuOpIdCard;// 制单员身份证
	private String docuOpMobile;// 制单员手机号
	private String checkerName;// 复核员姓名
	private String checkerIdCard;// 复核员身份证
	private String checkerMobile;// 复核员手机号
	private String dynamicCode;// 短信验证码（法人）
	private String corpIdType;
	private String corpIdNumber;
	private String docuOpIdType;
	private String docuOpIdNumber;
	private String checkerIdType;
	private String checkerIdNumber;

	
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getcNName() {
		return cNName;
	}
	public void setcNName(String cNName) {
		this.cNName = cNName;
	}
	public String getgBName() {
		return gBName;
	}
	public void setgBName(String gBName) {
		this.gBName = gBName;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getTrdCode() {
		return trdCode;
	}
	public void setTrdCode(String trdCode) {
		this.trdCode = trdCode;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getLegPerId() {
		return legPerId;
	}
	public void setLegPerId(String legPerId) {
		this.legPerId = legPerId;
	}
	public String getCorpIdCardLimitDate() {
		return corpIdCardLimitDate;
	}
	public void setCorpIdCardLimitDate(String corpIdCardLimitDate) {
		this.corpIdCardLimitDate = corpIdCardLimitDate;
	}
	public String getLegalPhone() {
		return legalPhone;
	}
	public void setLegalPhone(String legalPhone) {
		this.legalPhone = legalPhone;
	}
	public String getCfoName() {
		return cfoName;
	}
	public void setCfoName(String cfoName) {
		this.cfoName = cfoName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMedSmEntFlg() {
		return medSmEntFlg;
	}
	public void setMedSmEntFlg(String medSmEntFlg) {
		this.medSmEntFlg = medSmEntFlg;
	}
	public String getRegDist() {
		return regDist;
	}
	public void setRegDist(String regDist) {
		this.regDist = regDist;
	}
	public String getRegStrentDoor() {
		return regStrentDoor;
	}
	public void setRegStrentDoor(String regStrentDoor) {
		this.regStrentDoor = regStrentDoor;
	}
	public String getRegTePhone() {
		return regTePhone;
	}
	public void setRegTePhone(String regTePhone) {
		this.regTePhone = regTePhone;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerMobile() {
		return managerMobile;
	}
	public void setManagerMobile(String managerMobile) {
		this.managerMobile = managerMobile;
	}
	public String getManagerIdCard() {
		return managerIdCard;
	}
	public void setManagerIdCard(String managerIdCard) {
		this.managerIdCard = managerIdCard;
	}
	public String getAcctBank() {
		return acctBank;
	}
	public void setAcctBank(String acctBank) {
		this.acctBank = acctBank;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAcctName() {
		return acctName;
	}
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	public String getDepType() {
		return depType;
	}
	public void setDepType(String depType) {
		this.depType = depType;
	}
	public String getRegType() {
		return regType;
	}
	public void setRegType(String regType) {
		this.regType = regType;
	}
	
	public String getDocuOpName() {
		return docuOpName;
	}
	public void setDocuOpName(String docuOpName) {
		this.docuOpName = docuOpName;
	}
	public String getDocuOpIdCard() {
		return docuOpIdCard;
	}
	public void setDocuOpIdCard(String docuOpIdCard) {
		this.docuOpIdCard = docuOpIdCard;
	}
	public String getDocuOpMobile() {
		return docuOpMobile;
	}
	public void setDocuOpMobile(String docuOpMobile) {
		this.docuOpMobile = docuOpMobile;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public String getCheckerIdCard() {
		return checkerIdCard;
	}
	public void setCheckerIdCard(String checkerIdCard) {
		this.checkerIdCard = checkerIdCard;
	}
	public String getCheckerMobile() {
		return checkerMobile;
	}
	public void setCheckerMobile(String checkerMobile) {
		this.checkerMobile = checkerMobile;
	}
	
	public String getLimitDate() {
		return limitDate;
	}
	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}
	public BigDecimal getCapReg() {
		return capReg;
	}
	public void setCapReg(BigDecimal capReg) {
		this.capReg = capReg;
	}
	public String getDynamicCode() {
		return dynamicCode;
	}
	public void setDynamicCode(String dynamicCode) {
		this.dynamicCode = dynamicCode;
	}
	public String getCorpIdType() {
		return corpIdType;
	}
	public void setCorpIdType(String corpIdType) {
		this.corpIdType = corpIdType;
	}
	public String getCorpIdNumber() {
		return corpIdNumber;
	}
	public void setCorpIdNumber(String corpIdNumber) {
		this.corpIdNumber = corpIdNumber;
	}
	public String getDocuOpIdType() {
		return docuOpIdType;
	}
	public void setDocuOpIdType(String docuOpIdType) {
		this.docuOpIdType = docuOpIdType;
	}
	public String getDocuOpIdNumber() {
		return docuOpIdNumber;
	}
	public void setDocuOpIdNumber(String docuOpIdNumber) {
		this.docuOpIdNumber = docuOpIdNumber;
	}
	public String getCheckerIdType() {
		return checkerIdType;
	}
	public void setCheckerIdType(String checkerIdType) {
		this.checkerIdType = checkerIdType;
	}
	public String getCheckerIdNumber() {
		return checkerIdNumber;
	}
	public void setCheckerIdNumber(String checkerIdNumber) {
		this.checkerIdNumber = checkerIdNumber;
	}
	
}
