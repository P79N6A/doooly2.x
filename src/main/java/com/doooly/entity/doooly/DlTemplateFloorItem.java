/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.doooly;


import java.util.Date;

/**
 * 楼层关联itemEntity
 * @author Mr.Wu
 * @version 2019-03-07
 */
public class DlTemplateFloorItem {

	private static final long serialVersionUID = 1L;
	private String id;
	private String templateId;		// 模版id
	private String floorId;		// 楼层id
	private String title;		// 标题
	private String subTitle;		// 副标题
	private String linkUrl;		// 跳转链接
	private String iconUrl;		// 图标/图片url
	private String imageUrl;		// 图片url
	private String cornerMarkText;		// 角标文字
	private Integer state;		// 显示状态，(0：显示，1：不显示，默认0)
	private Integer sort;		// 排序
	private Integer relationType;		// 关联类型(0：无连接，1：连接自营商品，默认0)
	private String relationId;		// 关联数据的主键(长度为20是为了应对以后的分布式主键)
	private String depict;		// item描述
	private String createBy;	// 创建者
	private Date createDate;	// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;	// 更新日期
	private String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）

	// ====== 关联字段
	private String price;		// 商品价格
	private String originalPrice; // 原价
	private String name;		// 商品名称
	private Date ServerEndTime;	// 服务结束时间
	private String subUrl;		// 短链接
	private String isSupportIntegral;	// 是否支持积分

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getCornerMarkText() {
		return cornerMarkText;
	}

	public void setCornerMarkText(String cornerMarkText) {
		this.cornerMarkText = cornerMarkText;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getRelationType() {
		return relationType;
	}

	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
	}
	
	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getDepict() {
		return depict;
	}

	public void setDepict(String depict) {
		this.depict = depict;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getServerEndTime() {
		return ServerEndTime;
	}

	public void setServerEndTime(Date serverEndTime) {
		ServerEndTime = serverEndTime;
	}

	public String getSubUrl() {
		return subUrl;
	}

	public void setSubUrl(String subUrl) {
		this.subUrl = subUrl;
	}

	public String getIsSupportIntegral() {
		return isSupportIntegral;
	}

	public void setIsSupportIntegral(String isSupportIntegral) {
		this.isSupportIntegral = isSupportIntegral;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
}