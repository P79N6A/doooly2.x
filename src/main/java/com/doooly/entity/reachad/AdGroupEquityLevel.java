/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 企业权益信息Entity
 * @author sfc
 * @version 2019-03-09
 */
public class AdGroupEquityLevel {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private Integer adGroupId;		// 企业ID
	private Integer adEquityId;		// 权益ID
	private Integer adGroupEquityStatus;//企业权益状态
	private Integer adGroupLevel;		// 企业等级
	private String equityName;		// 权益名称:支持文本输入，最多20个字符
	private Integer equityType;		// 权益类型:默认为空，可选值为会员服务、商户返利
	private Integer displayOrder;		// 排序值:影响前台页面权益icon的排序先后情况，排序值越大排在越靠前；若不填写默认代入为0；当排序值相同时，权益ID值越小排序越靠前
	private Integer enableStatus;		// 使用状态:使用和不使用或者停用状态

	private String createBy;	// 创建者
	private Date createDate;	// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;	// 更新日期

	// ======关联字段
	private String equityLogo;	// 权益图标
	private String interfaceUrl;// 权益访问链接

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAdGroupId() {
		return adGroupId;
	}

	public void setAdGroupId(Integer adGroupId) {
		this.adGroupId = adGroupId;
	}

	public Integer getAdEquityId() {
		return adEquityId;
	}

	public void setAdEquityId(Integer adEquityId) {
		this.adEquityId = adEquityId;
	}

	public Integer getAdGroupEquityStatus() {
		return adGroupEquityStatus;
	}

	public void setAdGroupEquityStatus(Integer adGroupEquityStatus) {
		this.adGroupEquityStatus = adGroupEquityStatus;
	}

	public Integer getAdGroupLevel() {
		return adGroupLevel;
	}

	public void setAdGroupLevel(Integer adGroupLevel) {
		this.adGroupLevel = adGroupLevel;
	}

	public String getEquityName() {
		return equityName;
	}

	public void setEquityName(String equityName) {
		this.equityName = equityName;
	}

	public Integer getEquityType() {
		return equityType;
	}

	public void setEquityType(Integer equityType) {
		this.equityType = equityType;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
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

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getEquityLogo() {
		return equityLogo;
	}

	public void setEquityLogo(String equityLogo) {
		this.equityLogo = equityLogo;
	}

	public String getInterfaceUrl() {
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}
}