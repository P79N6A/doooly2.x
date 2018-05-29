package com.doooly.entity.reachad;

import java.util.HashMap;
import java.util.List;

public class InvitationRes {
	// 响应码
	private String code;
	private String desc;
	// 会员ID
	private String userId;
	// 会员卡号
	private String cardNo;
	// 邀请码
	private String invitationCode;
	// 最大邀请人数
	private Integer invitationMaxNum;
	// 可申请人数是否已满
	private boolean isFull;
	// 剩余邀请人数
	private Integer invitationAvailNum;
	// 已邀请人列表
	// private List<String> invitationFamilyList;
	// 已邀请人列表
	private List<HashMap<String, Object>> invitationFamilyList;

	// 邀请有效时间（与当前时间比较，默认7天）,单位为秒
	private Long validationTime;
	// 多个以逗号,分隔
	private String giftList;
	// 0:未送 1:已送
	private Integer giftState;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public Integer getInvitationMaxNum() {
		return invitationMaxNum;
	}

	public void setInvitationMaxNum(Integer invitationMaxNum) {
		this.invitationMaxNum = invitationMaxNum;
	}

	public Integer getInvitationAvailNum() {
		return invitationAvailNum;
	}

	public void setInvitationAvailNum(Integer invitationAvailNum) {
		this.invitationAvailNum = invitationAvailNum;
	}

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public Integer getGiftState() {
		return giftState;
	}

	public void setGiftState(Integer giftState) {
		this.giftState = giftState;
	}

	public Long getValidationTime() {
		return validationTime;
	}

	public void setValidationTime(Long validationTime) {
		this.validationTime = validationTime;
	}

	public String getGiftList() {
		return giftList;
	}

	public void setGiftList(String giftList) {
		this.giftList = giftList;
	}

	public List<HashMap<String, Object>> getInvitationFamilyList() {
		return invitationFamilyList;
	}

	public void setInvitationFamilyList(List<HashMap<String, Object>> invitationFamilyList) {
		this.invitationFamilyList = invitationFamilyList;
	}

}
