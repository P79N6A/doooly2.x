package com.doooly.entity.reachad;

import java.util.Date;

/**
 * @Description: 家庭关系表
 * @author: qing.zhang
 * @date: 2017-07-25
 */
public class AdFamilyUser {
    private Long id;
    private Long familyId;//家庭表主键ID
    private Long userId;//家庭表主键ID
    private Integer isPointShare;//积分共享开关 0：开 1：关
    private Integer familyRelationship;//与员工家庭关系 1：父亲或爸爸 2：母亲或妈妈 3：儿子 4：女儿 5：哥哥 6：姐姐 7：弟弟 8：妹妹
    private Integer delFlag;//0:有效 1：无效
    private Date updateDate;//更新时间
    private Date createDate;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getIsPointShare() {
        return isPointShare;
    }

    public void setIsPointShare(Integer isPointShare) {
        this.isPointShare = isPointShare;
    }

    public Integer getFamilyRelationship() {
        return familyRelationship;
    }

    public void setFamilyRelationship(Integer familyRelationship) {
        this.familyRelationship = familyRelationship;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
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
}
