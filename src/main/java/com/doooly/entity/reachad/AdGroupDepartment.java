package com.doooly.entity.reachad;

/**
 * 企业部门信息 ad_group_department
 * 
 * @author AlbertZhao
 * @date 2017年2月9日
 * @since 1.8
 * @version 1.0
 */
public final class AdGroupDepartment {

	private Long id; // 主键
	private AdGroup adGroup; // 企业信息
	private String departmentName; // 部门名称
	private String directorName; // 部门主管姓名
	private String directorTelephone; // 部门主管手机号
	private String directorEmail; // 部门主管邮箱
	private String managerName; // 部门管理员姓名
	private String managerTelephone; // 部门管理员手机号
	private String managerEmail; // 部门管理员邮箱
	private Long count; // 部门人数
	private Long superId;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getGroupId() {
		return adGroup != null ? adGroup.getId() : null;
	}

	public void setGroupID(Long id) {
		if (adGroup == null) {
			adGroup = new AdGroup();
		}
		adGroup.setId(id);
	}

	public AdGroup getAdGroup() {
		return adGroup;
	}

	public void setAdGroup(AdGroup adGroup) {
		this.adGroup = adGroup;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDirectorName() {
		return directorName;
	}

	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}

	public String getDirectorTelephone() {
		return directorTelephone;
	}

	public void setDirectorTelephone(String directorTelephone) {
		this.directorTelephone = directorTelephone;
	}

	public String getDirectorEmail() {
		return directorEmail;
	}

	public void setDirectorEmail(String directorEmail) {
		this.directorEmail = directorEmail;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerTelephone() {
		return managerTelephone;
	}

	public void setManagerTelephone(String managerTelephone) {
		this.managerTelephone = managerTelephone;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public Long getSuperId() {
		return superId;
	}

	public void setSuperId(Long superId) {
		this.superId = superId;
	}

	@Override
	public String toString() {
		return "AdGroupDepartment [id=" + id + ", adGroup=" + adGroup + ", departmentName=" + departmentName
				+ ", directorName=" + directorName + ", directorTelephone=" + directorTelephone + ", directorEmail="
				+ directorEmail + ", managerName=" + managerName + ", managerTelephone=" + managerTelephone
				+ ", managerEmail=" + managerEmail + ", count=" + count + ", superId=" + superId + "]";
	}

}
