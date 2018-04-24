package com.doooly.entity.reachad;

/**
 * 
    * @ClassName: AdBlocBlackUser  
    * @Description: 集团企业员工黑名单  
    * @author hutao  
    * @date 2017年10月19日  
    *
 */
public class AdBlocBlackUser {
	private Integer blocId;
	private Long userId;
	private String mobile;
	private String oldMobile;//旧手机号
	private String name;
	private String groupName;
	
	public AdBlocBlackUser(Integer blocId, Long userId, String mobile, String oldMobile, String name,
			String groupName) {
		super();
		this.blocId = blocId;
		this.userId = userId;
		this.mobile = mobile;
		this.oldMobile = oldMobile;
		this.name = name;
		this.groupName = groupName;
	}

	public AdBlocBlackUser(Integer blocId, Long userId, String mobile, String name, String groupName) {
		super();
		this.blocId = blocId;
		this.userId = userId;
		this.mobile = mobile;
		this.name = name;
		this.groupName = groupName;
	}
	
	public AdBlocBlackUser() {
		super();
	}

	public Integer getBlocId() {
		return blocId;
	}
	public void setBlocId(Integer blocId) {
		this.blocId = blocId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getOldMobile() {
		return oldMobile;
	}

	public void setOldMobile(String oldMobile) {
		this.oldMobile = oldMobile;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
