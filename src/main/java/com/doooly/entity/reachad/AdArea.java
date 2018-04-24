package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 轮播广告表Entity
 * 
 * @version 1.0
 */
public class AdArea {

	private int id;
	private String parentId;// 父id
	private String name;// 区域城市名
	private Date createDate;//开始时间
	private Date beginDate;// 更新时间
	private String serviceFlag;// 是否有商家服务该区域的标示
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public String getServiceFlag() {
		return serviceFlag;
	}
	public void setServiceFlag(String serviceFlag) {
		this.serviceFlag = serviceFlag;
	}
	
	

}