/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.doooly;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 模版信息Entity
 * @author Mr.Wu
 * @version 2019-03-07
 */
public class DlTemplateInfo {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;		// 模板名称
	private Integer isValid;		// 是否有效(0-草稿，1-完成；默认0，新增前将草稿数据删除)
	private Integer type;		// 0-3.0首页模版，1-3.0生活模版
	private String depict;		// 模版描述
	private String createBy;	// 创建者
	private Date createDate;	// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;	// 更新日期
	private String delFlag; 	// 删除标记

	// ===== 关联字段 =====
	private String groupNum;		// 企业id
	private List<DlTemplateFloor> floors = new ArrayList<>();	// 模版关联楼层

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDepict() {
		return depict;
	}

	public void setDepict(String depict) {
		this.depict = depict;
	}

	public List<DlTemplateFloor> getFloors() {
		return floors;
	}

	public void setFloors(List<DlTemplateFloor> floors) {
		this.floors = floors;
	}

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

}