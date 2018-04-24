package com.doooly.entity.reachad;

import java.util.Date;

public class AdBusinessLighten {
	
	private int id ;
	
	private String userId;
	
	private String businessId;
	
	private Integer type;
	
	private Date lightenTime;
	
	private String delFlag;
	
	private Date createTime;
	
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getLightenTime() {
		return lightenTime;
	}

	public void setLightenTime(Date lightenTime) {
		this.lightenTime = lightenTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
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

	@Override
	public String toString() {
		return "AdBusinessLighten [id=" + id + ", userId=" + userId
				+ ", businessId=" + businessId + ", type=" + type
				+ ", lightenTime=" + lightenTime + ", delFlag=" + delFlag
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}


}
