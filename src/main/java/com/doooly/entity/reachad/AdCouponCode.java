package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 优惠码Entity
 * 
 * @author lxl
 * @version 2016-12-14
 */
public class AdCouponCode {

	public static String LOCKED = "1";
	public static String UNLOCKED = "0";

	public static final int ISRECEIVED_NUMBER_LIMIT_OVER = 2;//领取数量超限

	public static final int ISRECEIVED_NUMBER = 1;//已经领取

	private Long id;

	private Long sysUserId;

	private Long userId;

	private Integer usedUserId;

	private Long coupon;

	private String code;

	private String isUsed;

	private Date usedDate;

	private String businessid;

	private String createBy;

	private String delFlag;

	private String remarks;

	private Date updateDate;

	private String updateBy;

	private Date createDate;

	private String telephone;

	private Long activityId;

	private Date receiveDate;

	private String groupId;

	private String isView;

	private String isLocked;

	private int isReceived;

	private AdCoupon adCoupon; //券类型

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUsedUserId() {
		return usedUserId;
	}

	public void setUsedUserId(Integer usedUserId) {
		this.usedUserId = usedUserId;
	}

	public Long getCoupon() {
		return coupon;
	}

	public void setCoupon(Long coupon) {
		this.coupon = coupon;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public Date getUsedDate() {
		return usedDate;
	}

	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	public String getBusinessid() {
		return businessid;
	}

	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getIsView() {
		return isView;
	}

	public void setIsView(String isView) {
		this.isView = isView;
	}

	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public int getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(int isReceived) {
		this.isReceived = isReceived;
	}

	public AdCoupon getAdCoupon() {
		return adCoupon;
	}

	public void setAdCoupon(AdCoupon adCoupon) {
		this.adCoupon = adCoupon;
	}
}