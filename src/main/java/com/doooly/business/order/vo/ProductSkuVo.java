package com.doooly.business.order.vo;

/**
 * 商品SKU
 * @author 2017-09-24 09:42:32 WANG
 */
public class ProductSkuVo {
	//商品ID
	private int productId;
	//SKU 
	private int skuId;
	//购买数量
	private int buyNum;
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getSkuId() {
		return skuId;
	}
	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
}
