package com.doooly.entity.reachad;

import java.util.Date;

/**
 * ad_brand品牌表
 * 
 * @author yangwenwei
 *
 */
public class AdBrand {
	private Long id;
	private String name; // 品牌名称
	private String brandImageUrl;// 品牌图片地址
	private String description;// 品牌描述
	private String businessId;// 品牌对应商家编号
	private Integer createUser;
	private Integer updateUser;
	private Date createDate;
	private Date updateDate;
	private String discount;
	private String businessBrandUrl;
	private String foreignName;
	private String isShow;// 是否显示，默认隐藏（1-显示，0-隐藏）
	private Integer sort;
	private String benefitWay;// 如何乐惠
	private String benefitContent;// 乐惠内容
	private String noticeInfo;// 注意事项
	private String recommendReason;// 推荐理由

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrandImageUrl() {
		return brandImageUrl;
	}

	public void setBrandImageUrl(String brandImageUrl) {
		this.brandImageUrl = brandImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Integer getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Integer updateUser) {
		this.updateUser = updateUser;
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

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getBusinessBrandUrl() {
		return businessBrandUrl;
	}

	public void setBusinessBrandUrl(String businessBrandUrl) {
		this.businessBrandUrl = businessBrandUrl;
	}

	public String getForeignName() {
		return foreignName;
	}

	public void setForeignName(String foreignName) {
		this.foreignName = foreignName;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getBenefitWay() {
		return benefitWay;
	}

	public void setBenefitWay(String benefitWay) {
		this.benefitWay = benefitWay;
	}

	public String getBenefitContent() {
		return benefitContent;
	}

	public void setBenefitContent(String benefitContent) {
		this.benefitContent = benefitContent;
	}

	public String getNoticeInfo() {
		return noticeInfo;
	}

	public void setNoticeInfo(String noticeInfo) {
		this.noticeInfo = noticeInfo;
	}

	public String getRecommendReason() {
		return recommendReason;
	}

	public void setRecommendReason(String recommendReason) {
		this.recommendReason = recommendReason;
	}

}
