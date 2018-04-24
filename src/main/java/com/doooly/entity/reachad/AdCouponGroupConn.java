package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 卡券活动表Entity
 * 
 * @author lxl
 * @version 2016-12-14
 */
public class AdCouponGroupConn {

	private int id;
	private int couponId;// 卡券ID
	private int groupId;// 企业ID
	private String userType;// 会员类型(2-全部，0-员工，1-家属)
	private int createUser;
	private Date createDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}