package com.doooly.entity.reachad;

import java.util.Date;

/**
 * @Description: 商户特权开通
 * @author: qing.zhang
 * @date: 2017-09-12
 */
public class AdBusinessPrivilegeActivity {

    private Integer businessId;//商户id
    private Integer userId;//用户id
    private Integer isOpenPrivilege;//是否开通商户特权
    private Integer privilegeType;//是否开通商户特权
    private Date createDate;//创建时间

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsOpenPrivilege() {
        return isOpenPrivilege;
    }

    public void setIsOpenPrivilege(Integer isOpenPrivilege) {
        this.isOpenPrivilege = isOpenPrivilege;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(Integer privilegeType) {
        this.privilegeType = privilegeType;
    }
}
