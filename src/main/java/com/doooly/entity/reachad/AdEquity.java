/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 权益管理Entity
 * @author sfc
 * @version 2019-03-07
 */
public class AdEquity {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private Integer equityId;		// 权益ID:保存后自动生成，按ID值自增长
	private String equityName;		// 权益名称:支持文本输入，最多20个字符
	private String equityLogo;		// 权益Logo
	private Integer equityType;		// 权益类型:默认为空，可选值为会员服务、商户返利
	private String limitType;		// 限制条件:默认为空；当选择商品返现时，显示限制条件内容，限制维度默认为月，次数默认为空
	private Integer displayOrder;		// 排序值:影响前台页面权益icon的排序先后情况，排序值越大排在越靠前；若不填写默认代入为0；当排序值相同时，权益ID值越小排序越靠前
	private String equityLevel;		// 权益级别
	private String equityDesc;		// 权益介绍
	private String userService;		// 用户服务
	private String instructions;		// 使用指南
	private String interfaceUrl;		// 页面链接:第一次保存后根据权益ID自动生成页面，点击&ldquo;预览&rdquo;可查看预览页面，点击保存后正式生效
	private Integer equityStatus;		// 权益状态:默认为关闭状态
	private String displayType;		// 对外展示:默认为否若权益状态关闭，则自动设置为否状态；当权益状态为开启同时对外展示状态为是才对外展示

	//新增标记企业是否已经绑定权益，本次是否已经添加上该权益(只用于企业新增权益列表)
	private Integer equityFlag;
	private Integer groupId;
	private Integer groupLevel;

	private String createBy;	// 创建者
	private Date createDate;	// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;	// 更新日期

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getEquityId() {
		return equityId;
	}

	public void setEquityId(Integer equityId) {
		this.equityId = equityId;
	}

	public String getEquityName() {
		return equityName;
	}

	public void setEquityName(String equityName) {
		this.equityName = equityName;
	}

	public String getEquityLogo() {
		return equityLogo;
	}

	public void setEquityLogo(String equityLogo) {
		this.equityLogo = equityLogo;
	}

	public Integer getEquityType() {
		return equityType;
	}

	public void setEquityType(Integer equityType) {
		this.equityType = equityType;
	}

	public String getLimitType() {
		return limitType;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getEquityLevel() {
		return equityLevel;
	}

	public void setEquityLevel(String equityLevel) {
		this.equityLevel = equityLevel;
	}

	public String getEquityDesc() {
		return equityDesc;
	}

	public void setEquityDesc(String equityDesc) {
		this.equityDesc = equityDesc;
	}

	public String getUserService() {
		return userService;
	}

	public void setUserService(String userService) {
		this.userService = userService;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getInterfaceUrl() {
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}

	public Integer getEquityStatus() {
		return equityStatus;
	}

	public void setEquityStatus(Integer equityStatus) {
		this.equityStatus = equityStatus;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public Integer getEquityFlag() {
		return equityFlag;
	}

	public void setEquityFlag(Integer equityFlag) {
		this.equityFlag = equityFlag;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(Integer groupLevel) {
		this.groupLevel = groupLevel;
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
}