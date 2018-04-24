package com.doooly.entity.reachad;

/**
 * 活动礼品Entity
 * 
 * @author lxl
 * @version 2016-12-15
 */
public class AdActivityGift {

	private Long id; // 主键
	private Integer activityId; // 活动ID
	private Integer productId; // 礼品ID
	private String productSn; // 礼品SN
	private Integer number; // 礼品份数
	private String businessId; // 商户编号 // 未生成商户实体 暂时String代替

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

}