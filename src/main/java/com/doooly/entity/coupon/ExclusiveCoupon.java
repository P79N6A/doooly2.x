package com.doooly.entity.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @className: ExclusiveCoupon
 * @description: 专属优惠券的javaBean
 * @author: wangchenyu
 * @date: 2018-02-27 17:59
 */
public class ExclusiveCoupon {
	/** ad_coupon_activity_conn表的id **/
	private Integer cacId;
	/** 本次活动剩余卡券数量 **/
	private Integer couponRemindQuantity;
	/** 是否已领取优惠券，0-未领取，1-已领取 **/
	private Integer isReceived;
	/** ad_coupon_activity表的主键 **/
	private Long activityId;
	/** 券活动名称 **/
	private String activityName;
	/** 优惠券单位，例如：￥ **/
	private String couponUnit;
	/** 优惠券金额，格式为：0.00 **/
	private BigDecimal couponAmount;
	/** 券活动开始时间 **/
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date activityBeginDate;
	/** 券活动结束时间 **/
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date activityEndDate;
	/** 券生效时间 **/
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date couponBeginDate;
	/** 券失效时间 **/
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date couponEndDate;
	/** 券活动列表页图片地址 **/
	private String activityListImageUrl;
	/** 券活动详情页图片地址 **/
	private String activityDetailImageUrl;
	/** 券活动介绍 **/
	private String activityIntroduction;
	/** 券活动链接 **/
	private String activityLinkUrl;
	/** ad_coupon表的主键 **/
	private Long couponId;
	/** 优惠券名称 **/
	private String couponName;
	/** 商品id **/
	private String productId;
	/** 商品名称 **/
	private String productName;
	/** ad_business表的主键 **/
	private Long businessId;
	/** 商户名称 **/
	private String businessName;

	public Integer getCacId() {
		return cacId;
	}

	public void setCacId(Integer cacId) {
		this.cacId = cacId;
	}

	public Integer getCouponRemindQuantity() {
		return couponRemindQuantity;
	}

	public void setCouponRemindQuantity(Integer couponRemindQuantity) {
		this.couponRemindQuantity = couponRemindQuantity;
	}

	public Integer getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(Integer isReceived) {
		this.isReceived = isReceived;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCouponUnit() {
		return couponUnit;
	}

	public void setCouponUnit(String couponUnit) {
		this.couponUnit = couponUnit;
	}

	public BigDecimal getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Date getActivityBeginDate() {
		return activityBeginDate;
	}

	public void setActivityBeginDate(Date activityBeginDate) {
		this.activityBeginDate = activityBeginDate;
	}

	public Date getActivityEndDate() {
		return activityEndDate;
	}

	public void setActivityEndDate(Date activityEndDate) {
		this.activityEndDate = activityEndDate;
	}

	public Date getCouponBeginDate() {
		return couponBeginDate;
	}

	public void setCouponBeginDate(Date couponBeginDate) {
		this.couponBeginDate = couponBeginDate;
	}

	public Date getCouponEndDate() {
		return couponEndDate;
	}

	public void setCouponEndDate(Date couponEndDate) {
		this.couponEndDate = couponEndDate;
	}

	public String getActivityListImageUrl() {
		return activityListImageUrl;
	}

	public void setActivityListImageUrl(String activityListImageUrl) {
		this.activityListImageUrl = activityListImageUrl;
	}

	public String getActivityDetailImageUrl() {
		return activityDetailImageUrl;
	}

	public void setActivityDetailImageUrl(String activityDetailImageUrl) {
		this.activityDetailImageUrl = activityDetailImageUrl;
	}

	public String getActivityIntroduction() {
		return activityIntroduction;
	}

	public void setActivityIntroduction(String activityIntroduction) {
		this.activityIntroduction = activityIntroduction;
	}

	public String getActivityLinkUrl() {
		return activityLinkUrl;
	}

	public void setActivityLinkUrl(String activityLinkUrl) {
		this.activityLinkUrl = activityLinkUrl;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
}
