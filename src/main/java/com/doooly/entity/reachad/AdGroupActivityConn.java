package com.doooly.entity.reachad;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 企业Entity
 * @author qing.zhang
 * @version 2017-04-25
 */
public class AdGroupActivityConn {
    private Long id; //主键id
	private Integer activityId;		// 活动ID
	private Integer couponId;		// 卡券ID
	private Integer groupId;		// 企业ID
	private String userType;		// 会员类型(2-全部，0-员工，1-家属)
	private Integer createUser;		// 创建人
	private Integer otherActivityId;		// other_activity_id
    private Date createDate;	// 创建日期
    private String groupName;//企业名称

    private Integer companyId;		// 公司id

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	
	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	
	@NotNull(message="企业ID不能为空")
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
	
	public Integer getOtherActivityId() {
		return otherActivityId;
	}

	public void setOtherActivityId(Integer otherActivityId) {
		this.otherActivityId = otherActivityId;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}