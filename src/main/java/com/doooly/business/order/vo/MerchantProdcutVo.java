package com.doooly.business.order.vo;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商家商品
 * @author 2017-09-24 09:54:32 WANG
 *
 */
public class MerchantProdcutVo {
	
	private int merchantId;
	private String remarks;
	private List<ProductSkuVo> productSku ;
	
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<ProductSkuVo> getProductSku() {
		return productSku;
	}
	public void setProductSku(List<ProductSkuVo> productSku) {
		this.productSku = productSku;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}
	
	

	
}
