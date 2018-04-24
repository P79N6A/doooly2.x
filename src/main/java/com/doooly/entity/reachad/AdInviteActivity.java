package com.doooly.entity.reachad;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 活动Entity
 * @author qing.zhang
 * @version 2017-04-25
 */
public class AdInviteActivity {
	
	private Long id;//主键
	private String actNum;		// 活动编号
	private String actName;		// 活动名称
	private String status;		// 活动开启状态(0-未开启，1-已开启)
	private Integer bagId;		// bag_id
	private Long groupId;		// 企业编号
	private Integer serial;		// 活动期数
	private Integer totalPeopleQuantity;		// 活动人数
	private Integer totalCodeQuantity;		// 邀请码总数量
	private Integer usedCodeQuantity;		// 已使用邀请码数量
	private Integer newUserQuantity;		// 新会员数量
	private Date validDate;		// 活动有效期至
	private Date beginTime;		// 活动开始时间
	private Date endTime;		// 活动结束时间
	private Integer functionStage;		// 提货券功能开发阶段(3期功能以3为标记)
	private String actDesc;		// 作为提货券报表【券种类】显示
	private Integer browserCount;		// 浏览次数
	private String address;		// 活动地址
	private String plateformType;		// 适用平台（0-微信，1-PC，2-APP）
	private String linkUrl;		// 活动链接
	private String introduction;		// 活动介绍
	private String listImage;		// 列表图片
	private String detailImage;		// 详情图片
	private Integer supportCount;		// 活动被点赞数
	private Integer registerType;		// 活动类型0-普通活动，1-报名活动
    private Date createDate;//创建时间
    private Date updateDate;	// 更新日期
    private Integer sort;		// 排序号

	public String getActNum() {
		return actNum;
	}

	public void setActNum(String actNum) {
		this.actNum = actNum;
	}
	
	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getBagId() {
		return bagId;
	}

	public void setBagId(Integer bagId) {
		this.bagId = bagId;
	}
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	@NotNull(message="活动期数不能为空")
	public Integer getSerial() {
		return serial;
	}

	public void setSerial(Integer serial) {
		this.serial = serial;
	}
	
	public Integer getTotalPeopleQuantity() {
		return totalPeopleQuantity;
	}

	public void setTotalPeopleQuantity(Integer totalPeopleQuantity) {
		this.totalPeopleQuantity = totalPeopleQuantity;
	}
	
	@NotNull(message="邀请码总数量不能为空")
	public Integer getTotalCodeQuantity() {
		return totalCodeQuantity;
	}

	public void setTotalCodeQuantity(Integer totalCodeQuantity) {
		this.totalCodeQuantity = totalCodeQuantity;
	}
	
	public Integer getUsedCodeQuantity() {
		return usedCodeQuantity;
	}

	public void setUsedCodeQuantity(Integer usedCodeQuantity) {
		this.usedCodeQuantity = usedCodeQuantity;
	}
	
	public Integer getNewUserQuantity() {
		return newUserQuantity;
	}

	public void setNewUserQuantity(Integer newUserQuantity) {
		this.newUserQuantity = newUserQuantity;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="活动有效期至不能为空")
	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="活动开始时间不能为空")
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="活动结束时间不能为空")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Integer getFunctionStage() {
		return functionStage;
	}

	public void setFunctionStage(Integer functionStage) {
		this.functionStage = functionStage;
	}
	
	public String getActDesc() {
		return actDesc;
	}

	public void setActDesc(String actDesc) {
		this.actDesc = actDesc;
	}
	
	public Integer getBrowserCount() {
		return browserCount;
	}

	public void setBrowserCount(Integer browserCount) {
		this.browserCount = browserCount;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPlateformType() {
		return plateformType;
	}

	public void setPlateformType(String plateformType) {
		this.plateformType = plateformType;
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public String getListImage() {
		return listImage;
	}

	public void setListImage(String listImage) {
		this.listImage = listImage;
	}
	
	public String getDetailImage() {
		return detailImage;
	}

	public void setDetailImage(String detailImage) {
		this.detailImage = detailImage;
	}
	
	public Integer getSupportCount() {
		return supportCount;
	}

	public void setSupportCount(Integer supportCount) {
		this.supportCount = supportCount;
	}
	
	public Integer getRegisterType() {
		return registerType;
	}

	public void setRegisterType(Integer registerType) {
		this.registerType = registerType;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}