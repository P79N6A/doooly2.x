/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 企业权益关系结构Entity
 * @author sfc
 * @version 2019-03-12
 */
public class AdGroupEquityRelation {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private Integer adGroupId;		// 企业ID
	private Integer adEquityId;		// 权益ID
	private Integer adRelationStatus;		// 关联状态
	private String createBy;	// 创建者
	private Date createDate;	// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;	// 更新日期

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Integer getAdRelationStatus() {
		return adRelationStatus;
	}

	public void setAdRelationStatus(Integer adRelationStatus) {
		this.adRelationStatus = adRelationStatus;
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