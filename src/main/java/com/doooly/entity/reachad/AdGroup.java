package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业信息表POJO类
 * @author 赵清江
 * @date 2016年7月14日
 * @version 1.0
 */
public class AdGroup {
	/**
	 * 编号
	 */
    private Long id;
    /**
     * 企业名称
     */
    private String groupName;
    /**
     * 企业编号
     */
    private String groupNum;
    /**
     * 账户状态
     */
    private String isActive;
    /**
     * 正式员工起始积分定制
     */
    private BigDecimal officialIntegral;
    /**
     * 正式员工号定制
     */
    private String officialNumber;
    /**
     * 编号开始值
     */
    private String relationStartNo;
    /**
     * 编号结束值
     */
    private String relationEndNo;
    /**
     * 父企业编号
     */
    private Long superGroupId;
    /**
     * 识别码
     */
    private String udId;
    /**
     * 非正式员工起始积分定制
     */
    private BigDecimal unOfficialIntegral;
    /**
     * 激活日期
     */
    private Date activeDate;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 删除标记
     */
    private String delFlag;
    /**
     * 备注信息
     */
    private String remarks;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createDate;
    private String logoUrl;
    private String miniLogoUrl;
    private String groupShortName;
    private String displayUrl;

    // app开机启动图片链接地址
    private String appStartUpUrl;
    //app开机启动图片链接地址版本号
    private String appStartUpVerions;
    //集团主键
    private Integer blocId;

    private BigDecimal dailyLimit;
    private BigDecimal charges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum == null ? null : groupNum.trim();
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive == null ? null : isActive.trim();
    }

    public BigDecimal getOfficialIntegral() {
        return officialIntegral;
    }

    public void setOfficialIntegral(BigDecimal officialIntegral) {
        this.officialIntegral = officialIntegral;
    }

    public String getOfficialNumber() {
        return officialNumber;
    }

    public void setOfficialNumber(String officialNumber) {
        this.officialNumber = officialNumber == null ? null : officialNumber.trim();
    }

    public String getRelationStartNo() {
        return relationStartNo;
    }

    public void setRelationStartNo(String relationStartNo) {
        this.relationStartNo = relationStartNo == null ? null : relationStartNo.trim();
    }

    public String getRelationEndNo() {
        return relationEndNo;
    }

    public void setRelationEndNo(String relationEndNo) {
        this.relationEndNo = relationEndNo == null ? null : relationEndNo.trim();
    }

    public Long getSuperGroupId() {
        return superGroupId;
    }

    public void setSuperGroupId(Long superGroupId) {
        this.superGroupId = superGroupId;
    }

    public String getUdId() {
        return udId;
    }

    public void setUdId(String udId) {
        this.udId = udId == null ? null : udId.trim();
    }

    public BigDecimal getUnOfficialIntegral() {
        return unOfficialIntegral;
    }

    public void setUnOfficialIntegral(BigDecimal unOfficialIntegral) {
        this.unOfficialIntegral = unOfficialIntegral;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getMiniLogoUrl() {
		return miniLogoUrl;
	}

	public void setMiniLogoUrl(String miniLogoUrl) {
		this.miniLogoUrl = miniLogoUrl;
	}

	public String getGroupShortName() {
		return groupShortName;
	}

	public void setGroupShortName(String groupShortName) {
		this.groupShortName = groupShortName;
	}

	public String getDisplayUrl() {
		return displayUrl;
	}

	public void setDisplayUrl(String displayUrl) {
		this.displayUrl = displayUrl;
	}

    public String getAppStartUpUrl() {
        return appStartUpUrl;
    }

    public void setAppStartUpUrl(String appStartUpUrl) {
        this.appStartUpUrl = appStartUpUrl;
    }

    public String getAppStartUpVerions() {
        return appStartUpVerions;
    }

    public void setAppStartUpVerions(String appStartUpVerions) {
        this.appStartUpVerions = appStartUpVerions;
    }

	public Integer getBlocId() {
		return blocId;
	}

	public void setBlocId(Integer blocId) {
		this.blocId = blocId;
	}

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public BigDecimal getCharges() {
        return charges;
    }

    public void setCharges(BigDecimal charges) {
        this.charges = charges;
    }
}