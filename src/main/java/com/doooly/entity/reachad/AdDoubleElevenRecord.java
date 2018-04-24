package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 轮播广告表Entity
 * 
 * @version 1.0
 */
public class AdDoubleElevenRecord {

	private int id;
	private String userId;// 用户id
	private String type;// 0是发起者,1是参与者
	private BigDecimal pointCount;// 参与者参与的积分
	private String superUserId;// 发起者用户id，发起者数据为null
	private Date createDate;
	private BigDecimal totalCount;
	private Integer memberCount;
	private String telephone;
	private String name;
	private String headImgurl; // 头像图片
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getPointCount() {
		return pointCount;
	}
	public void setPointCount(BigDecimal pointCount) {
		this.pointCount = pointCount;
	}
	public String getSuperUserId() {
		return superUserId;
	}
	public void setSuperUserId(String superUserId) {
		this.superUserId = superUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public BigDecimal getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadImgurl() {
		return headImgurl;
	}
	public void setHeadImgurl(String headImgurl) {
		this.headImgurl = headImgurl;
	}


}