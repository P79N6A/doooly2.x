package com.doooly.entity.reachad;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 报名记录Entity
 * @author qing.zhang
 * @version 2017-04-25
 */
public class AdJoinUser {

    private Long id; //主键id
	private Integer joinUser;		// 报名用户ID
	private Integer itemId;		// 报名项目ID
	private Integer activityId;		// 活动ID
	private Integer supportCount;		// 项目下参与者被点赞数
    private Date updateDate;	// 更新日期
    private String userName;		// 报名人姓名
    private String mobile;		// 手机号
    private String employeeNumber;		// 工号
    private Integer groupId;		// 企业ID
    private Integer companyId;		// 公司ID
    private Integer departmentId;		// 部门ID
    private String groupName; //企业名称
    private String companyName; //公司名称
    private String departmentName; //部门名称
    private String telephone; //电话 获取微信id用
    private String cardNumber; //卡号 获取微信id用
    private String nickName; // 微信昵称
    private String headImgurl; // 头像图片


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull(message="报名用户ID不能为空")
	public Integer getJoinUser() {
		return joinUser;
	}

	public void setJoinUser(Integer joinUser) {
		this.joinUser = joinUser;
	}
	
	@NotNull(message="报名项目ID不能为空")
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	@NotNull(message="活动ID不能为空")
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	
	@NotNull(message="项目下参与者被点赞数不能为空")
	public Integer getSupportCount() {
		return supportCount;
	}

	public void setSupportCount(Integer supportCount) {
		this.supportCount = supportCount;
	}

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgurl() {
        return headImgurl;
    }

    public void setHeadImgurl(String headImgurl) {
        this.headImgurl = headImgurl;
    }
}