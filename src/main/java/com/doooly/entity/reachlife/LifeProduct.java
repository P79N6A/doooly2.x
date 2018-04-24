package com.doooly.entity.reachlife;

import java.util.Date;

/**
 * 商品Entity
 * 
 * @author lxl
 * @version 2016-12-14
 */
public class LifeProduct {

	private long id = 1L;
	private Date modifyDate; // 修改时间
	private String allocatedStock; // 已分配库存
	private String attributeValue0; // 权限
	private String attributeValue1; // 内容
	private String attributeValue10; // attribute_value10
	private String attributeValue11; // attribute_value11
	private String attributeValue12; // attribute_value12
	private String attributeValue13; // attribute_value13
	private String attributeValue14; // attribute_value14
	private String attributeValue15; // attribute_value15
	private String attributeValue16; // attribute_value16
	private String attributeValue17; // attribute_value17
	private String attributeValue18; // attribute_value18
	private String attributeValue19; // attribute_value19
	private String attributeValue2; // 保险条款
	private String attributeValue3; // attribute_value3
	private String attributeValue4; // attribute_value4
	private String attributeValue5; // attribute_value5
	private String attributeValue6; // attribute_value6
	private String attributeValue7; // attribute_value7
	private String attributeValue8; // attribute_value8
	private String attributeValue9; // attribute_value9
	private String cost; // 成本价
	private String fullName; // 保险公司名称/全程
	private Long hits; // 点击数
	private String image; // 图片
	private String introduction; // 介绍
	private String isGift; // 是否为赠品
	private String isList; // 是否列出
	private String isMarketable; // 是否上架
	private String isTop; // 是否置顶
	private String keyword; // 搜索关键字
	private String marketPrice; // 市场价
	private String memo; // 库存备注
	private Long monthHits; // 月点击数
	private Date monthHitsDate; // 月点击数更新日期
	private Long monthSales; // 月销量
	private Date monthSalesDate; // 月销量更新日期
	private String name; // 名称
	private Long point; // 积分
	private String price; // 销售价
	private Long sales; // 销量
	private String score; // 评分
	private Long scoreCount; // 评分数
	private String seoDescription; // 页面描述
	private String seoKeywords; // 页面关键字
	private String seoTitle; // 页面标题
	private String sn; // 编号
	private String stock; // 库存
	private String stockMemo; // 库存备注
	private Long totalScore; // 总评分
	private String unit; // 单位
	private Long weekHits; // 周点击数
	private Date weekHitsDate; // 周点击数更新时间
	private Long weekSales; // 周销量
	private Date weekSalesDate; // 周销量更新日期
	private String weight; // 重量
	private Long productCategory; // 活动类别
	private String buyUpperLimit; // 购买上限数
	private String attention; // 注意事项
	private Date buyEndTime; // 活动开始时间
	private Date buyStartTime; // 活动截止时间
	private String buyType; // 购买模式
	private String buyUrl; // 购买模式的商家PC网址
	private String buyWechatUrl; // 购买模式的商家微信网址
	private String exchangeFlow; // 兑换流程
	private String privilegeExplain; // 会员特权说明
	private String serviceRange; // 服务范围
	private String surplusQuantity; // surplus_quantity
	private String totalQuantity; // 总数量
	private String usableMethod; // 使用方法
	private Date validEndTime; // 兑换截至时间
	private Date validStartTime; // 兑换开始时间
	private String wechatImage; // 微信端列表图片
	private Long supplierId; // 供应商id
	private String wechatDetailImage; // 微信端详情图片
	private String coverage; // 保险金额
	private String settlementCount; // 保险理赔次数
	private String deleteFlag; // 删除标识

	public LifeProduct() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getAllocatedStock() {
		return allocatedStock;
	}

	public void setAllocatedStock(String allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	public String getAttributeValue0() {
		return attributeValue0;
	}

	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	public String getAttributeValue1() {
		return attributeValue1;
	}

	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	public String getAttributeValue10() {
		return attributeValue10;
	}

	public void setAttributeValue10(String attributeValue10) {
		this.attributeValue10 = attributeValue10;
	}

	public String getAttributeValue11() {
		return attributeValue11;
	}

	public void setAttributeValue11(String attributeValue11) {
		this.attributeValue11 = attributeValue11;
	}

	public String getAttributeValue12() {
		return attributeValue12;
	}

	public void setAttributeValue12(String attributeValue12) {
		this.attributeValue12 = attributeValue12;
	}

	public String getAttributeValue13() {
		return attributeValue13;
	}

	public void setAttributeValue13(String attributeValue13) {
		this.attributeValue13 = attributeValue13;
	}

	public String getAttributeValue14() {
		return attributeValue14;
	}

	public void setAttributeValue14(String attributeValue14) {
		this.attributeValue14 = attributeValue14;
	}

	public String getAttributeValue15() {
		return attributeValue15;
	}

	public void setAttributeValue15(String attributeValue15) {
		this.attributeValue15 = attributeValue15;
	}

	public String getAttributeValue16() {
		return attributeValue16;
	}

	public void setAttributeValue16(String attributeValue16) {
		this.attributeValue16 = attributeValue16;
	}

	public String getAttributeValue17() {
		return attributeValue17;
	}

	public void setAttributeValue17(String attributeValue17) {
		this.attributeValue17 = attributeValue17;
	}

	public String getAttributeValue18() {
		return attributeValue18;
	}

	public void setAttributeValue18(String attributeValue18) {
		this.attributeValue18 = attributeValue18;
	}

	public String getAttributeValue19() {
		return attributeValue19;
	}

	public void setAttributeValue19(String attributeValue19) {
		this.attributeValue19 = attributeValue19;
	}

	public String getAttributeValue2() {
		return attributeValue2;
	}

	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}

	public String getAttributeValue3() {
		return attributeValue3;
	}

	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}

	public String getAttributeValue4() {
		return attributeValue4;
	}

	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}

	public String getAttributeValue5() {
		return attributeValue5;
	}

	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}

	public String getAttributeValue6() {
		return attributeValue6;
	}

	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}

	public String getAttributeValue7() {
		return attributeValue7;
	}

	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}

	public String getAttributeValue8() {
		return attributeValue8;
	}

	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}

	public String getAttributeValue9() {
		return attributeValue9;
	}

	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
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

	public String getIsGift() {
		return isGift;
	}

	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}

	public String getIsList() {
		return isList;
	}

	public void setIsList(String isList) {
		this.isList = isList;
	}

	public String getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(String isMarketable) {
		this.isMarketable = isMarketable;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getMonthHits() {
		return monthHits;
	}

	public void setMonthHits(Long monthHits) {
		this.monthHits = monthHits;
	}

	public Date getMonthHitsDate() {
		return monthHitsDate;
	}

	public void setMonthHitsDate(Date monthHitsDate) {
		this.monthHitsDate = monthHitsDate;
	}

	public Long getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(Long monthSales) {
		this.monthSales = monthSales;
	}

	public Date getMonthSalesDate() {
		return monthSalesDate;
	}

	public void setMonthSalesDate(Date monthSalesDate) {
		this.monthSalesDate = monthSalesDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Long getScoreCount() {
		return scoreCount;
	}

	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getStockMemo() {
		return stockMemo;
	}

	public void setStockMemo(String stockMemo) {
		this.stockMemo = stockMemo;
	}

	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getWeekHits() {
		return weekHits;
	}

	public void setWeekHits(Long weekHits) {
		this.weekHits = weekHits;
	}

	public Date getWeekHitsDate() {
		return weekHitsDate;
	}

	public void setWeekHitsDate(Date weekHitsDate) {
		this.weekHitsDate = weekHitsDate;
	}

	public Long getWeekSales() {
		return weekSales;
	}

	public void setWeekSales(Long weekSales) {
		this.weekSales = weekSales;
	}

	public Date getWeekSalesDate() {
		return weekSalesDate;
	}

	public void setWeekSalesDate(Date weekSalesDate) {
		this.weekSalesDate = weekSalesDate;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Long getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(Long productCategory) {
		this.productCategory = productCategory;
	}

	public String getBuyUpperLimit() {
		return buyUpperLimit;
	}

	public void setBuyUpperLimit(String buyUpperLimit) {
		this.buyUpperLimit = buyUpperLimit;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public Date getBuyEndTime() {
		return buyEndTime;
	}

	public void setBuyEndTime(Date buyEndTime) {
		this.buyEndTime = buyEndTime;
	}

	public Date getBuyStartTime() {
		return buyStartTime;
	}

	public void setBuyStartTime(Date buyStartTime) {
		this.buyStartTime = buyStartTime;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}

	public String getBuyWechatUrl() {
		return buyWechatUrl;
	}

	public void setBuyWechatUrl(String buyWechatUrl) {
		this.buyWechatUrl = buyWechatUrl;
	}

	public String getExchangeFlow() {
		return exchangeFlow;
	}

	public void setExchangeFlow(String exchangeFlow) {
		this.exchangeFlow = exchangeFlow;
	}

	public String getPrivilegeExplain() {
		return privilegeExplain;
	}

	public void setPrivilegeExplain(String privilegeExplain) {
		this.privilegeExplain = privilegeExplain;
	}

	public String getServiceRange() {
		return serviceRange;
	}

	public void setServiceRange(String serviceRange) {
		this.serviceRange = serviceRange;
	}

	public String getSurplusQuantity() {
		return surplusQuantity;
	}

	public void setSurplusQuantity(String surplusQuantity) {
		this.surplusQuantity = surplusQuantity;
	}

	public String getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getUsableMethod() {
		return usableMethod;
	}

	public void setUsableMethod(String usableMethod) {
		this.usableMethod = usableMethod;
	}

	public Date getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}

	public Date getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(Date validStartTime) {
		this.validStartTime = validStartTime;
	}

	public String getWechatImage() {
		return wechatImage;
	}

	public void setWechatImage(String wechatImage) {
		this.wechatImage = wechatImage;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getWechatDetailImage() {
		return wechatDetailImage;
	}

	public void setWechatDetailImage(String wechatDetailImage) {
		this.wechatDetailImage = wechatDetailImage;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getSettlementCount() {
		return settlementCount;
	}

	public void setSettlementCount(String settlementCount) {
		this.settlementCount = settlementCount;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}