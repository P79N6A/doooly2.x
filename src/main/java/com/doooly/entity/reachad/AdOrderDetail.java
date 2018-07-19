/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单明细管理Entity
 * @author shinex
 * @version 2015-12-17
 */
public class AdOrderDetail {
	
	private static final long serialVersionUID = 1L;
	private AdOrderReport adOrderReport;		// 订单报表编号
	private AdBusinessCategory adBusinessCategory;		// 商品品类
	private String code;		// 商品编号
	private String goods;		// 商品名称
	private BigDecimal amount;		// 实收价格
	private BigDecimal price;		// 应收价格
	private BigDecimal number;		// 数量
	private BigDecimal tax;   //税率
	private String specification;   //规格
	private String cardCode;   //卡号
	private String cardPass;   //卡密
    private String productImg;    // 商品主图链接
    private String retMsg;// 充值平台返回msg
    private String retCode;// 充值平台返回code
	private String retState;// 充值平台处理状态
    private String duihuanUrl;// 虚拟产品兑换地址
	

	public AdOrderReport getAdOrderReport() {
		return adOrderReport;
	}

	public void setAdOrderReport(AdOrderReport adOrderReport) {
		this.adOrderReport = adOrderReport;
	}
	
	public AdBusinessCategory getAdBusinessCategory() {
		return adBusinessCategory;
	}

	public void setAdBusinessCategory(AdBusinessCategory adBusinessCategory) {
		this.adBusinessCategory = adBusinessCategory;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getCardPass() {
		return cardPass;
	}

	public void setCardPass(String cardPass) {
		this.cardPass = cardPass;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getDuihuanUrl() {
		return duihuanUrl;
	}

	public void setDuihuanUrl(String duihuanUrl) {
		this.duihuanUrl = duihuanUrl;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetState() {
		return retState;
	}

	public void setRetState(String retState) {
		this.retState = retState;
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("goods", getGoods());
		map.put("specification", this.getSpecification());
		map.put("cardCode", this.getCardCode());
		map.put("cardPass", this.getCardPass());
		map.put("productImg", this.getProductImg());
		map.put("unitPrice", getPrice().divide(getNumber(),BigDecimal.ROUND_HALF_DOWN).toString());
		map.put("number", getNumber().toString());
		map.put("price", getPrice().toString());
		map.put("amount", getAmount().toString());
		map.put("retMsg", this.getRetMsg());
		map.put("duihuanUrl", this.getDuihuanUrl());
		map.put("retState", this.getRetState());
		map.put("retCode", this.getRetCode());
		return map;
	}
	
}