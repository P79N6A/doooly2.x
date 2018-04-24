/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachlife;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author 赵一鹏
 * @date 2017年8月29日
 * @version 1.0
 */
public class LifeGroup {

	private String id;
	private Date modifyDate; // 修改时间
	private String groupName; // 企业名
	private String groupNum; // 企业番号
	private BigDecimal officialIntegral; // 正式员工起始积分定制
	private String officialNumber; // 正式社员工号定制识别码
	private Long superGroupId; // 父企业ID
	private String udId; // 识别码
	private BigDecimal unOfficialIntegral; // unofficial_integral
	private String isActive; // 账户状态 1.未激活、2.激活、3.冻结 4.冻结
	private String adId; // B系统对应id
	private String delFlg; // 删除标记
	private String groupShortName; // 公司简称(微信端显示[企业专享]logo)
	private String dataSyn;// 是否同步到第三方（0：不同步，1：同步）
	private String remarks;

	public LifeGroup() {
		super();
	}


	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	public BigDecimal getOfficialIntegral() {
		return officialIntegral;
	}

	public void setOfficialIntegral(BigDecimal officialIntegral) {
		this.officialIntegral = officialIntegral;
	}

	public String getOfficialNumber() {
		return officialNumber;
	}

	public void setOfficialNumber(String officialNumber) {
		this.officialNumber = officialNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public Long getSuperGroupId() {
		return superGroupId;
	}

	public void setSuperGroupId(Long superGroupId) {
		this.superGroupId = superGroupId;
	}

	public String getUdId() {
		return udId;
	}

	public void setUdId(String udId) {
		this.udId = udId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public BigDecimal getUnofficialIntegral() {
		return unOfficialIntegral;
	}

	public void setUnofficialIntegral(BigDecimal unOfficialIntegral) {
		this.unOfficialIntegral = unOfficialIntegral;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getGroupShortName() {
		return groupShortName;
	}

	public void setGroupShortName(String groupShortName) {
		this.groupShortName = groupShortName;
	}

	public String getDataSyn() {
		return dataSyn;
	}

	public void setDataSyn(String dataSyn) {
		this.dataSyn = dataSyn;
	}

}