/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.doooly;

import java.util.Date;

/**
 * 模版关联企业管理Entity
 * @author Mr.Wu
 * @version 2019-03-07
 */
public class DlTemplateGroup {

	private static final long serialVersionUID = 1L;
	private String id;
	private String templateId;		// 模板主键
	private String groupId;		// 企业主键
	private Integer templateType;		// 1-3.0首页模版，2-3.0生活模版
	private String createBy;	// 创建者
	private Date createDate;	// 创建日期

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
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