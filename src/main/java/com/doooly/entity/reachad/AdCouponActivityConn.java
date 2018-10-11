package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 卡券活动关联表Entity
 * 
 * @author lxl
 * @version 2016-12-14
 */
public class AdCouponActivityConn {

	private int id;
	private int activityId;// 卡券活动ID
	private int couponId;// 卡券ID
	private int couponQuantity;// 本次活动分配的卡券数量
	private int couponUsedQuantity;// 本次活动已使用的卡券数量（默认为0）
	private int couponRemindQuantity;// 本次活动剩余卡券数量（默认为0）
	private Date createDate;
	private String wechatDetailImage;// 微信端优惠券图片地址
	private String pcDetailImage;// PC端优惠券图片地址
	private String productSn; // 商品编号
	private int type;// 用户是否已使用
	private String beginDate; // 生效开始时间
	private String endDate; // 生效结束时间
	private AdCoupon coupon;// 卡券
	private String registerNum;// 报名人数
	private String registerFlag;// 报名标记(1：已报名,0：未报名)
	private String couponCode;// 卡券券码
	private String couponCodeId;// 卡券券码id
	private String couponTagName;// 卡券标签名
	private String firstCategoryName;// 卡券一级分类名称
	private String businessIntroduce;// 商家介绍
	private String codeReceiveDate;// 券码领取时间
	private Integer level;
	private AdBusiness adBusiness; //商家
	private String isView; //是否被查看(0:未被查看 1:被查看)
	private int couponCount;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public int getCouponQuantity() {
		return couponQuantity;
	}

	public void setCouponQuantity(int couponQuantity) {
		this.couponQuantity = couponQuantity;
	}

	public int getCouponUsedQuantity() {
		return couponUsedQuantity;
	}

	public void setCouponUsedQuantity(int couponUsedQuantity) {
		this.couponUsedQuantity = couponUsedQuantity;
	}

	public int getCouponRemindQuantity() {
		return couponRemindQuantity;
	}

	public void setCouponRemindQuantity(int couponRemindQuantity) {
		this.couponRemindQuantity = couponRemindQuantity;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getWechatDetailImage() {
		return wechatDetailImage;
	}

	public void setWechatDetailImage(String wechatDetailImage) {
		this.wechatDetailImage = wechatDetailImage;
	}

	public String getPcDetailImage() {
		return pcDetailImage;
	}

	public void setPcDetailImage(String pcDetailImage) {
		this.pcDetailImage = pcDetailImage;
	}

	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public AdCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(AdCoupon coupon) {
		this.coupon = coupon;
	}

	public String getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(String registerNum) {
		this.registerNum = registerNum;
	}

	public String getRegisterFlag() {
		return registerFlag;
	}

	public void setRegisterFlag(String registerFlag) {
		this.registerFlag = registerFlag;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponTagName() {
		return couponTagName;
	}

	public void setCouponTagName(String couponTagName) {
		this.couponTagName = couponTagName;
	}

	public String getFirstCategoryName() {
		return firstCategoryName;
	}

	public void setFirstCategoryName(String firstCategoryName) {
		this.firstCategoryName = firstCategoryName;
	}

	public String getBusinessIntroduce() {
		return businessIntroduce;
	}

	public void setBusinessIntroduce(String businessIntroduce) {
		this.businessIntroduce = businessIntroduce;
	}

	public String getCodeReceiveDate() {
		return codeReceiveDate;
	}

	public void setCodeReceiveDate(String codeReceiveDate) {
		this.codeReceiveDate = codeReceiveDate;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public AdBusiness getAdBusiness() {
		return adBusiness;
	}

	public void setAdBusiness(AdBusiness adBusiness) {
		this.adBusiness = adBusiness;
	}

	public String getIsView() {
		return isView;
	}

	public void setIsView(String isView) {
		this.isView = isView;
	}

	public String getCouponCodeId() {
		return couponCodeId;
	}

	public void setCouponCodeId(String couponCodeId) {
		this.couponCodeId = couponCodeId;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}
	
}