package com.doooly.business.product.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业商品价格配置Entity
 * 
 * @author qing.zhang
 * @version 2017-11-21
 */
public class AdGroupSelfProductPrice {

	private int id;
	private int skuId; // 自营商品skuID
	private String groupId; // 企业ID
	private BigDecimal specialPrice; // 特惠价格
	private Date specialStartDate; // 特惠开始时间
	private Date specialEndDate; // 特惠结束时间
	private String tag; // 标签
	private BigDecimal discount; // 折扣
	private Integer inventory; // 活动配置库存
	private Integer skuInventory; // 商品库存
	private Integer inventoryInit;
	private Integer number; // 标识编号，判断多条配置属于同一条记录
	private Integer status; // 状态标记（0-草稿，1-完成,2-删除）
	private String updateUser; // 修改人
	private String specification; // SKU规格
	private String productNumber; // SKU编号
	private String title; // 标题
	private String detail;//商品详情
	private String introduction;//商品简述
	private BigDecimal marketPrice; // 市场价
	private BigDecimal supplyPrice; // 供货价
	private BigDecimal sellPrice; // 销售价
	private Integer type;// 价格优惠类型
	private Integer buyNumberLimit;// 购买数量限制
	private Date activityStartDate; // 活动开始时间
	private Date activityEndDate; // 活动结束时间
	private String activityName;// 活动名称
	private String isStart;
	private String image; // 图片
	private int produceId;//商品id
	private int businessId;
	private String url;
	private String weekList;
    private String activityOfTime;

	public Integer getInventoryInit() {
		return inventoryInit;
	}

	public void setInventoryInit(Integer inventoryInit) {
		this.inventoryInit = inventoryInit;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSkuId() {
		return skuId;
	}

	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public BigDecimal getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(BigDecimal specialPrice) {
		this.specialPrice = specialPrice;
	}

	public Date getSpecialStartDate() {
		return specialStartDate;
	}

	public void setSpecialStartDate(Date specialStartDate) {
		this.specialStartDate = specialStartDate;
	}

	public Date getSpecialEndDate() {
		return specialEndDate;
	}

	public void setSpecialEndDate(Date specialEndDate) {
		this.specialEndDate = specialEndDate;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getSkuInventory() {
		return skuInventory;
	}

	public void setSkuInventory(Integer skuInventory) {
		this.skuInventory = skuInventory;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(BigDecimal supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getBuyNumberLimit() {
		return buyNumberLimit;
	}

	public void setBuyNumberLimit(Integer buyNumberLimit) {
		this.buyNumberLimit = buyNumberLimit;
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

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getIsStart() {
		return isStart;
	}

	public void setIsStart(String isStart) {
		this.isStart = isStart;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getProduceId() {
		return produceId;
	}

	public void setProduceId(int produceId) {
		this.produceId = produceId;
	}

	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWeekList() {
		return weekList;
	}

	public void setWeekList(String weekList) {
		this.weekList = weekList;
	}

	public String getActivityOfTime() {
		return activityOfTime;
	}

	public void setActivityOfTime(String activityOfTime) {
		this.activityOfTime = activityOfTime;
	}

}