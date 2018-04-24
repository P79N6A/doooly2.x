/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;

/**
 * 商户品类信息 entity
 * 
 * @author yuelou.zhang
 * @version 2017年11月13日
 */
public class AdBusinessCategory {

	private AdBusiness adBusiness; // 商户编号
	private String categoryId; // 品类编号
	private String categoryName; // 品类名称
	private String goodsNumber; // 商品货号

	public AdBusinessCategory() {
		super();
	}

	public AdBusinessCategory(AdBusiness businessId) {
		this.adBusiness = businessId;
	}

	public AdBusiness getAdBusiness() {
		return adBusiness;
	}

	public void setAdBusiness(AdBusiness adBusiness) {
		this.adBusiness = adBusiness;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

}