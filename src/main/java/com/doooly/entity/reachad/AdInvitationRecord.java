package com.doooly.entity.reachad;

import java.util.Date;


/**
 * 家属邀请记录实体类
 * 
 * @author yuelou.zhang
 * @date 2016年10月31日
 * @version 1.0
 */
public class AdInvitationRecord {

	/**
	 * 
	 */
	
	private String id;
	private String remarks;	// 备注
	private Date createDate;	// 创建日期
	private Date updateDate;	// 更新日期
	private String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）
	private String channel; // channel

	private Boolean isNewRecord;
	/**
	 * 邀请人会员id
	 */
	private int inviterId;

	/**
	 * 被邀请人会员id
	 */
	private int inviteeId;

	/**
	 * 兑换码
	 */
	private String code;

	/**
	 * 邀请人可获取积分(0:被邀请人未核销;5:被邀请人核销成功)
	 */
	private String point;

	/**
	 * 兑换码更新时间
	 */
	private String codeUpdateDate;

	/**
	 * 被邀请人手机号码
	 */
	private String telephone;
	//家属标记（1-全家总动员活动家属）
	private String flag;
	//链接渠道类型
	private String type;
	
	
	/**
	 * 兑换码核销标志
	 */
	private String isUsed;
	
	private String name;
	
	private String userId;
	
	private String isActive;

	
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsNewRecord() {
		return isNewRecord;
	}

	public void setIsNewRecord(Boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getInviterId() {
		return inviterId;
	}

	public void setInviterId(int inviterId) {
		this.inviterId = inviterId;
	}

	public int getInviteeId() {
		return inviteeId;
	}

	public void setInviteeId(int inviteeId) {
		this.inviteeId = inviteeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getCodeUpdateDate() {
		return codeUpdateDate;
	}

	public void setCodeUpdateDate(String codeUpdateDate) {
		this.codeUpdateDate = codeUpdateDate;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}
