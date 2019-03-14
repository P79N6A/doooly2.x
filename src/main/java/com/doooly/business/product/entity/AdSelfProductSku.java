/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.business.product.entity;


/**
 * 商品sku管理Entity
 * @author wenwei.yang
 * @version 2017-09-20
 */
public class AdSelfProductSku  {
	
	private String id;
	private String specification;		// SKU规格
	private String number;		// SKU编号
	private String title;		// 标题
	private String state;		// 状态
	private String marketPrice;		// 市场价
	private String supplyPrice;		// 供货价
	private String sellPrice;		// 销售价
	private String freight;		// 运费
	private String deliveryType;		// 配送方式(0.物流配送,1.发放电子凭证)
	private String auditDescription;		// 审核备注
	private String selfProductId;		// 自营商品ID
	private String externalNumber;		// 欧飞提供商品编号
	private String updateUser;		// 修改人
	private String updateDate;
	private Integer inventory;// 库存
	private String specialPrice;// 特惠价
	private Integer buyNumberLimit;// 购买数量限制
	private boolean overBuyLimit;// 是否超过限购条件(默认为false 可以购买)

	// ====== 关联字段
	private String introduction;	// 商品简述
	private String image;			// 商品主图

	
	
	public AdSelfProductSku() {
	}

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	public String getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(String supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
	
	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}
	
	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	
	public String getAuditDescription() {
		return auditDescription;
	}

	public void setAuditDescription(String auditDescription) {
		this.auditDescription = auditDescription;
	}
	
	public String getSelfProductId() {
		return selfProductId;
	}

	public void setSelfProductId(String selfProductId) {
		this.selfProductId = selfProductId;
	}
	
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getExternalNumber() {
		return externalNumber;
	}
	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public String getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}

	public Integer getBuyNumberLimit() {
		return buyNumberLimit;
	}

	public void setBuyNumberLimit(Integer buyNumberLimit) {
		this.buyNumberLimit = buyNumberLimit;
	}

	public boolean isOverBuyLimit() {
		return overBuyLimit;
	}

	public void setOverBuyLimit(boolean overBuyLimit) {
		this.overBuyLimit = overBuyLimit;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
}