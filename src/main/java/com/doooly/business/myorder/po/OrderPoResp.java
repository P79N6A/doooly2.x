package com.doooly.business.myorder.po;

import java.math.BigDecimal;
import java.util.Date;

/***
 * 订单
 */
public class OrderPoResp {
		private Long id;
	// 订单id
		private Long orderId;
		//订单编号
		private String orderNumber;
		// 会员ID
		private Long userId;
		//订单完成状态：0 未支付(进行中)   1已支付 2取消 
		private String  state;
		//交易阶段 1：完成订单    5：退货订单, 自营订单状态(10:待付款,20:待发货,30已发货,40已收货,50确认收货,99交易取消, 交易完成和退款使用原来状态1,5)
		private int type;
		//是否返利 0:是   1：否
		private String isUserRebate;
		//预返积分
		private BigDecimal userRebate;
		//实付金额
		private BigDecimal totalPrice;
		//应付金额
		private BigDecimal totalMount;
		//订单日期
		private Date orderDate;
		//商户名称
		private String storeName;
		//订单来源   0:机票个人      1：机票分销商       2：合作商家     3：睿渠平台
		private Integer isSource;
		private String logo;
		private Integer productType;
		private String businessId;
		private BigDecimal savePrice;
		private String company;
		private String productImg;
		// 商品名称
		private String goods;
		// 规格
		private String specification;
		private String cashDeskSource;
		
		
					   
		public Long getOrderId() {
			return orderId;
		}
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getIsUserRebate() {
			return isUserRebate;
		}
		public void setIsUserRebate(String isUserRebate) {
			this.isUserRebate = isUserRebate;
		}
		public BigDecimal getUserRebate() {
			return userRebate;
		}
		public void setUserRebate(BigDecimal userRebate) {
			this.userRebate = userRebate;
		}
		
		public BigDecimal getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(BigDecimal totalPrice) {
			this.totalPrice = totalPrice;
		}
		public BigDecimal getTotalMount() {
			return totalMount;
		}
		public void setTotalMount(BigDecimal totalMount) {
			this.totalMount = totalMount;
		}
		public Date getOrderDate() {
			return orderDate;
		}
		public void setOrderDate(Date orderDate) {
			this.orderDate = orderDate;
		}
		public String getStoreName() {
			return storeName;
		}
		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}
		public Integer getIsSource() {
			return isSource;
		}
		public void setIsSource(Integer isSource) {
			this.isSource = isSource;
		}
		public String getLogo() {
			return logo;
		}
		public void setLogo(String logo) {
			this.logo = logo;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Integer getProductType() {
			return productType;
		}
		public void setProductType(Integer productType) {
			this.productType = productType;
		}
		public String getBusinessId() {
			return businessId;
		}
		public void setBusinessId(String businessId) {
			this.businessId = businessId;
		}
		public BigDecimal getSavePrice() {
			return savePrice;
		}
		public void setSavePrice(BigDecimal savePrice) {
			this.savePrice = savePrice;
		}
		
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getProductImg() {
			return productImg;
		}
		public void setProductImg(String productImg) {
			this.productImg = productImg;
		}
		public String getGoods() {
			return goods;
		}
		public void setGoods(String goods) {
			this.goods = goods;
		}
		public String getSpecification() {
			return specification;
		}
		public void setSpecification(String specification) {
			this.specification = specification;
		}
		public String getCashDeskSource() {
			return cashDeskSource;
		}
		public void setCashDeskSource(String cashDeskSource) {
			this.cashDeskSource = cashDeskSource;
		}
	
		
		
}
