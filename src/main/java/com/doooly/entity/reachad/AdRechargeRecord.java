package com.doooly.entity.reachad;

import java.util.Date;

/**
 * ad_recharge_record表POJO类
 * 
 * @author yangwenwei
 * @date 2018年4月16日
 * @version 1.0
 */
public class AdRechargeRecord {
	

	private Long id;

	private Long userId;
	
	private Long sourceUserId;

	private String openId;
	
	private String sourceOpenId;
	
	private String orderNumber;

	private String activityParam;
	
	private String state;
	
	private String delFlag;
	
	private String channel;

	private Date updateDate;

	private Date createDate;

	public AdRechargeRecord(String userId, String orderNumber, Integer state,String openId,String sourceOpenId,
			String sourceUserId,String channel,String activityParam) {
		this.orderNumber = orderNumber;
		this.state = state+"";
		this.userId = Long.valueOf(userId);
		this.sourceOpenId= sourceOpenId;
		this.sourceUserId = Long.valueOf(sourceUserId);
		this.openId = openId;
		this.channel = channel;
		this.activityParam = activityParam;
		this.delFlag = "0";
	}
	public AdRechargeRecord() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getSourceUserId() {
		return sourceUserId;
	}
	public void setSourceUserId(Long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getSourceOpenId() {
		return sourceOpenId;
	}
	public void setSourceOpenId(String sourceOpenId) {
		this.sourceOpenId = sourceOpenId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getActivityParam() {
		return activityParam;
	}
	public void setActivityParam(String activityParam) {
		this.activityParam = activityParam;
	}

	@Override
	public String toString() {
		return "AdRechargeRecord{" +
				"id=" + id +
				", userId=" + userId +
				", sourceUserId=" + sourceUserId +
				", openId='" + openId + '\'' +
				", sourceOpenId='" + sourceOpenId + '\'' +
				", orderNumber='" + orderNumber + '\'' +
				", activityParam='" + activityParam + '\'' +
				", state='" + state + '\'' +
				", delFlag='" + delFlag + '\'' +
				", channel='" + channel + '\'' +
				", updateDate=" + updateDate +
				", createDate=" + createDate +
				'}';
	}
}