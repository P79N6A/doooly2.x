package com.doooly.business.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 订单详情表
 * 
 * @author 2017-09-24 09:54:52 WANG
 *
 */
public class OrderItemVo {
	
	private long id;
	//订单主键
	private long orderReportId;
	//分类
	private String categoryId;
	//商品编号
	private String code;
	//商品名称
	private String goods;
	//总额
	private BigDecimal amount;
	//单价
	private BigDecimal price;
	//购买数量
	private BigDecimal number;
	//创建人
	private String createBy;
	//
	private int delFlag;
	//备注
	private String remarks;
	private BigDecimal tax;
	//更新时间
	private Date updateDate;
	//修改人
	private String updateBy;
	//创建时间
	private Date createDate;
	//SKU
	private String sku;
	//skuId
	private String productSkuId;
	//充值平台返回相关信息
	private String externalNumber;//欧飞产品id    
	private String retCode;
	private String retMsg;
	private String retState;
	private String cardOid;
	private String cardCode;
	private String cardPass;
	//商品主图
	private String productImg;
	//兑换地址
	private String duihuanUrl;

	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOrderReportId() {
		return orderReportId;
	}
	public void setOrderReportId(long orderReportId) {
		this.orderReportId = orderReportId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String getRetState() {
		return retState;
	}
	public void setRetState(String retState) {
		this.retState = retState;
	}
	public String getCardOid() {
		return cardOid;
	}
	public void setCardOid(String cardOid) {
		this.cardOid = cardOid;
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
	public String getProductSkuId() {
		return productSkuId;
	}
	public void setProductSkuId(String productSkuId) {
		this.productSkuId = productSkuId;
	}
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	
	public String getExternalNumber() {
		return externalNumber;
	}
	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getDuihuanUrl() {
		return duihuanUrl;
	}

	public void setDuihuanUrl(String duihuanUrl) {
		this.duihuanUrl = duihuanUrl;
	}
}
