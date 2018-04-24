package com.doooly.entity.reachad;

import java.math.BigDecimal;

/**
 * re_merchant_cps_detail商家营销规则详情表POJO类
 * 
 * @author tangzhiyuan
 *
 */
public class ReMerchantCpsDetail {

	private Long id;
	// '单据排序号（首单、第二单...）'
	private Long orderNumber;
	// '订单配置项编号'
	private Long itemNumber;
	// '计算标准(0:按应付;1:按实付)'
	private int countType;
	// '阀值'
	private BigDecimal thresholdValue;
	// '营销比例'
	private BigDecimal cpsScale;
	// '营销费用'
	private BigDecimal cpaFee;
	// '品类编号'
	private String categoryId;
	// '商家营销费用总表id'
	private Long merchantCpsId;

	public ReMerchantCpsDetail() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Long itemNumber) {
		this.itemNumber = itemNumber;
	}

	public int getCountType() {
		return countType;
	}

	public void setCountType(int countType) {
		this.countType = countType;
	}

	public BigDecimal getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(BigDecimal thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	public BigDecimal getCpsScale() {
		return cpsScale;
	}

	public void setCpsScale(BigDecimal cpsScale) {
		this.cpsScale = cpsScale;
	}

	public BigDecimal getCpaFee() {
		return cpaFee;
	}

	public void setCpaFee(BigDecimal cpaFee) {
		this.cpaFee = cpaFee;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Long getMerchantCpsId() {
		return merchantCpsId;
	}

	public void setMerchantCpsId(Long merchantCpsId) {
		this.merchantCpsId = merchantCpsId;
	}

	@Override
	public String toString() {
		return "ReMerchantCpsDetail [id=" + id + ", orderNumber=" + orderNumber + ", itemNumber=" + itemNumber
				+ ", countType=" + countType + ", thresholdValue=" + thresholdValue + ", cpsScale=" + cpsScale
				+ ", cpaFee=" + cpaFee + ", categoryId=" + categoryId + ", merchantCpsId=" + merchantCpsId + "]";
	}
	
	
}
