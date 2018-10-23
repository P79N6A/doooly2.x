package com.doooly.entity.reachad;

public class AdUserEnterpriseChange {

	private Long userId;
	private Long oldGroupId;
	private Long newGroupId;
	private Integer isActive;
	
	public AdUserEnterpriseChange() {
		super();
	}
	public AdUserEnterpriseChange(Long userId, Long oldGroupId, Long newGroupId, Integer isActive) {
		super();
		this.userId = userId;
		this.oldGroupId = oldGroupId;
		this.newGroupId = newGroupId;
		this.isActive = isActive;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOldGroupId() {
		return oldGroupId;
	}
	public void setOldGroupId(Long oldGroupId) {
		this.oldGroupId = oldGroupId;
	}
	public Long getNewGroupId() {
		return newGroupId;
	}
	public void setNewGroupId(Long newGroupId) {
		this.newGroupId = newGroupId;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
}
