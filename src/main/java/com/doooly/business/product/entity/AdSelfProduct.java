/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.business.product.entity;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 自营商品管理Entity
 * @author wenwei.yang
 * @version 2017-09-20
 */
public class AdSelfProduct  {
	
	private String id;  //id
	private String name;		// 商品名称
	private String businessId;		// 商家ID
	private String brandId;		// 品牌ID
	private String isMarketable;		// 是否上架(1-上架，0-下架)，默认为0
	private String firstCategory;		// 商品一级分类
	private String secondCategory;		// 商品二级分类
	private String thirdCategory;		// 商品三级分类
	private String externalNumber;		// 外部编号
	private Date buyStartDate;		// 上架开始时间
	private Date buyEndDate;		// 上架结束时间
	private String detail;		// 商品描述、备注
	private String createUser;		// 创建人
	private String updateUser;		// 修改人
	private String image;		// 自营商品主图
	private String sellPrice; // 最低商品销售价
	private String marketPrice;	// 最低商品销售价对应的市场价
	private String buyStartDateStr;	// 上架开始时间(字符串格式)
	private String buyEndDateStr; // 上架结束时间(字符串格式)
	private String company;	// 商户名称
	private String brandDescription;// 品牌介绍
	private String duihuanUrl;// 兑换地址

	private String introduction;// 商品简述
	private String limitSkuListStr;// 商品被屏蔽skuId集合
	private String specialPrice;// 商品最低特惠价(排除被屏蔽sku)
	private String discount;// 商品最低折扣(排除被屏蔽sku)
	private String inventory;// 商品总库存(排除被屏蔽sku)
	private String tagName;// 特惠商品标签名称
	private int state;
	private Date createDate;
	private Date updateDate;
	private Integer delFlag;
	// SKU
	private List<AdSelfProductSku> productSku;
	private Date activityStartDate;// 活动开始时间
	private Date activityEndDate;// 活动结束时间
	private Long countdownTime;//倒计时
	private Date currentTime;//当前时间
	
	public AdSelfProduct() {
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
	public String getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(String isMarketable) {
		this.isMarketable = isMarketable;
	}
	
	public String getFirstCategory() {
		return firstCategory;
	}

	public void setFirstCategory(String firstCategory) {
		this.firstCategory = firstCategory;
	}
	
	public String getSecondCategory() {
		return secondCategory;
	}

	public void setSecondCategory(String secondCategory) {
		this.secondCategory = secondCategory;
	}
	
	public String getThirdCategory() {
		return thirdCategory;
	}

	public void setThirdCategory(String thirdCategory) {
		this.thirdCategory = thirdCategory;
	}
	
	public String getExternalNumber() {
		return externalNumber;
	}

	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
	}
	
	public Date getBuyStartDate() {
		return buyStartDate;
	}

	public void setBuyStartDate(Date buyStartDate) {
		this.buyStartDate = buyStartDate;
	}
	
	public Date getBuyEndDate() {
		return buyEndDate;
	}

	public void setBuyEndDate(Date buyEndDate) {
		this.buyEndDate = buyEndDate;
	}
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getBuyStartDateStr() {
		return buyStartDateStr;
	}

	public void setBuyStartDateStr(String buyStartDateStr) {
		this.buyStartDateStr = buyStartDateStr;
	}

	public String getBuyEndDateStr() {
		return buyEndDateStr;
	}

	public void setBuyEndDateStr(String buyEndDateStr) {
		this.buyEndDateStr = buyEndDateStr;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBrandDescription() {
		return brandDescription;
	}

	public void setBrandDescription(String brandDescription) {
		this.brandDescription = brandDescription;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
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


	public Integer getDelFlag() {
		return delFlag;
	}


	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}


	public List<AdSelfProductSku> getProductSku() {
		return productSku;
	}


	public void setProductSku(List<AdSelfProductSku> productSku) {
		this.productSku = productSku;
	}

	public String getIntroduction() {
		return introduction;
	}


	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}


	public String getLimitSkuListStr() {
		return limitSkuListStr;
	}


	public void setLimitSkuListStr(String limitSkuListStr) {
		this.limitSkuListStr = limitSkuListStr;
	}


	public String getSpecialPrice() {
		return specialPrice;
	}


	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}


	public String getDiscount() {
		return discount;
	}


	public void setDiscount(String discount) {
		this.discount = discount;
	}


	public String getInventory() {
		return inventory;
	}


	public void setInventory(String inventory) {
		this.inventory = inventory;
	}


	public String getTagName() {
		return tagName;
	}


	public void setTagName(String tagName) {
		this.tagName = tagName;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getDuihuanUrl() {
		return duihuanUrl;
	}

	public void setDuihuanUrl(String duihuanUrl) {
		this.duihuanUrl = duihuanUrl;
	}

	public Date getActivityStartDate() {
		return activityStartDate;
	}

	public void setActivityStartDate(Date activityStartDate) {
		this.activityStartDate = activityStartDate;
	}

	public Date getActivityEndDate() {
		return activityEndDate;
	}

	public void setActivityEndDate(Date activityEndDate) {
		this.activityEndDate = activityEndDate;
	}

	public Long getCountdownTime() {
		return countdownTime;
	}

	public void setCountdownTime(Long countdownTime) {
		this.countdownTime = countdownTime;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}
}