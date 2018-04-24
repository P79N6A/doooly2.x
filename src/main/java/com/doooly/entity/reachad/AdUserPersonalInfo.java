package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 会员(员工)个人信息 ad_user_personal_info(ad_user附录表)
 * 
 * @author AlbertZhao
 * @date 2017年2月9日
 * @since 1.8
 * @version 1.0
 */
public final class AdUserPersonalInfo {

	private Long id;
	private AdGroupDepartment adGroupDepartment;
	private String workNumber;
	private Date birthday;
	private Integer isRegularEmployee;
	private Date entryDate;
	private Integer hasChild;
	private Integer isAudit;// '审核状态 0：审核中，1：审核通过，2：审核不通过',
	private Integer dataSources;// '数据来源 0:平台导入，1：员工自主申请',
	private String authFlag;// 认证标识,默认非认证会员(0-非认证会员,1-认证会员),

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDepartmentId() {
		return adGroupDepartment != null ? adGroupDepartment.getId() : null;
	}

	public void setDepartmentID(Long id) {
		if (adGroupDepartment == null) {
			adGroupDepartment = new AdGroupDepartment();
		}
		adGroupDepartment.setId(id);
	}

	public AdGroupDepartment getAdGroupDepartment() {
		return adGroupDepartment;
	}

	public void setAdGroupDepartment(AdGroupDepartment adGroupDepartmentPJ) {
		this.adGroupDepartment = adGroupDepartmentPJ;
	}

	public String getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getIsRegularEmployee() {
		return isRegularEmployee;
	}

	public void setIsRegularEmployee(Integer isRegularEmployee) {
		this.isRegularEmployee = isRegularEmployee;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Integer getHasChild() {
		return hasChild;
	}

	public void setHasChild(Integer hasChild) {
		this.hasChild = hasChild;
	}

	public Integer getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
	}

	public Integer getDataSources() {
		return dataSources;
	}

	public void setDataSources(Integer dataSources) {
		this.dataSources = dataSources;
	}

	public String getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}

	@Override
	public String toString() {
		return "AdUserPersonalInfo [id=" + id + ", adGroupDepartment=" + adGroupDepartment + ", workNumber="
				+ workNumber + ", birthday=" + birthday + ", isRegularEmployee=" + isRegularEmployee + ", entryDate="
				+ entryDate + ", hasChild=" + hasChild + ", isAudit=" + isAudit + ", dataSources=" + dataSources
				+ ", authFlag=" + authFlag + "]";
	}

}
